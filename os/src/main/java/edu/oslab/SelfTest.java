package edu.oslab;

import javax.crypto.SecretKey;
import java.util.List;

public final class SelfTest {
    private SelfTest() { }

    public static void run() throws Exception {
        testProcessLifecycle();
        testThreadLifecycle();
        testScheduling();
        testDeadlockDetection();
        testMemoryManagement();
        testFileSystem();
        testIo();
        testCommunication();
        testSecurity();
        System.out.println("All OS lab self-tests passed.");
    }

    private static void testProcessLifecycle() {
        ProcessManager manager = new ProcessManager();
        ProcessControlBlock parent = manager.create("parent");
        ProcessControlBlock child = manager.createChild(parent.processId(), "child");
        manager.dispatch(parent.processId());
        manager.terminate(child.processId());
        check(child.state() == ProcessState.ZOMBIE, "Child should become a zombie");
        manager.reap(parent.processId(), child.processId());
        check(child.state() == ProcessState.TERMINATED, "Reaped child should terminate");
        ProcessControlBlock survivor = manager.createChild(parent.processId(), "survivor");
        manager.terminate(parent.processId());
        check(survivor.orphaned(), "A child of an exited process should be marked orphaned");
        expectStateFailure(() -> manager.createChild(parent.processId(), "invalid"),
                "An exited process must not create a child");
    }

    private static void testThreadLifecycle() {
        ThreadManager manager = new ThreadManager();
        ThreadManager.SimulatedThread thread =
                manager.create(1, "test-thread", ThreadManager.Level.USER);
        manager.start(thread.threadId());
        manager.block(thread.threadId());
        manager.wake(thread.threadId());
        check(thread.state() == ThreadManager.State.READY, "Woken thread should be ready");
    }

    private static void testScheduling() {
        SchedulingLab scheduler = new SchedulingLab();
        for (SchedulingLab.Algorithm algorithm : SchedulingLab.Algorithm.values()) {
            SchedulingLab.ScheduleResult result = scheduler.run(
                    SchedulingLab.sampleJobs(), algorithm, 2);
            check(result.jobs().size() == 4, algorithm + " should schedule every job");
            check(result.jobs().stream().allMatch(job -> job.waitingTime() >= 0),
                    algorithm + " should not produce negative waiting times");
        }
        SchedulingLab.ScheduleResult roundRobin =
                scheduler.run(SchedulingLab.sampleJobs(), SchedulingLab.Algorithm.ROUND_ROBIN, 2);
        check(roundRobin.timeline().get(0).equals("P1[0-2]")
                        && roundRobin.timeline().get(1).equals("P2[2-4]"),
                "Round-robin should rotate ready jobs at the quantum boundary");
    }

    private static void testDeadlockDetection() {
        check(DeadlockLab.detectCycle(3, List.of(
                new DeadlockLab.ResourceRequest(0, 1),
                new DeadlockLab.ResourceRequest(1, 2),
                new DeadlockLab.ResourceRequest(2, 0))).size() == 4,
                "Cycle should include its repeated start node");
        check(DeadlockLab.detectCycle(3, List.of(
                new DeadlockLab.ResourceRequest(0, 1),
                new DeadlockLab.ResourceRequest(1, 2))).isEmpty(),
                "Acyclic graph should not report a cycle");
    }

    private static void testMemoryManagement() {
        MemoryManager memory = new MemoryManager(100);
        memory.allocate("a", 40);
        memory.allocate("b", 30);
        memory.release("a");
        memory.release("b");
        check(memory.freeBytes() == 100 && memory.blocks().size() == 1,
                "Adjacent free blocks should coalesce");
    }

    private static void testFileSystem() {
        VirtualFileSystem fileSystem = new VirtualFileSystem();
        fileSystem.create("/docs/readme.txt", "lab");
        check(fileSystem.read("/docs/readme.txt").equals("lab"), "File contents should round-trip");
        expectFailure(() -> fileSystem.read("/docs/../secret"), "Traversal path should be rejected");
    }

    private static void testIo() {
        IoSystem io = new IoSystem();
        io.submit("P1", "read", "disk");
        io.submit("P2", "write", "disk");
        check(io.completeNext("disk").process().equals("P1"), "I/O requests should complete FIFO");
    }

    private static void testCommunication() {
        CommunicationLab bus = new CommunicationLab();
        CommunicationLab.Endpoint sender =
                new CommunicationLab.Endpoint(CommunicationLab.EndpointType.PROCESS, 1, 0);
        CommunicationLab.Endpoint receiver =
                new CommunicationLab.Endpoint(CommunicationLab.EndpointType.THREAD, 2, 3);
        bus.send(sender, receiver, "message");
        check(bus.receive(receiver).payload().equals("message"),
                "Message should arrive in the receiver mailbox");
    }

    private static void testSecurity() throws Exception {
        SecurityLab security = new SecurityLab();
        security.grant("reader", SecurityLab.Permission.READ);
        security.register("user", "test-password".toCharArray(), "reader");
        check(security.authenticate("user", "test-password".toCharArray()),
                "Correct password should authenticate");
        check(!security.authenticate("user", "wrong-password".toCharArray()),
                "Incorrect password should not authenticate");
        check(!security.isAuthorized("user", SecurityLab.Permission.READ),
                "A failed authentication should clear the authenticated session");
        check(security.authenticate("user", "test-password".toCharArray()),
                "Correct password should authenticate after a failed attempt");
        check(security.isAuthorized("user", SecurityLab.Permission.READ),
                "Authenticated user should receive granted permissions");
        check(!security.isAuthorized("user", SecurityLab.Permission.WRITE),
                "Unassigned permission should be denied");
        SecretKey key = security.generateAesKey();
        SecurityLab.EncryptedMessage message = security.encrypt(key, "secret");
        check(security.decrypt(key, message).equals("secret"), "AES-GCM should round-trip");
        byte[] altered = message.ciphertext();
        altered[0] ^= 1;
        try {
            security.decrypt(key, new SecurityLab.EncryptedMessage(message.iv(), altered));
            throw new AssertionError("AES-GCM should reject modified ciphertext");
        } catch (javax.crypto.AEADBadTagException expected) {
            // Expected: GCM authentication detects the changed ciphertext.
        }
    }

    private static void expectFailure(Runnable action, String message) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError(message);
    }

    private static void expectStateFailure(Runnable action, String message) {
        try {
            action.run();
        } catch (IllegalStateException expected) {
            return;
        }
        throw new AssertionError(message);
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
