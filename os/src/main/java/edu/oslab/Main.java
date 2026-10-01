package edu.oslab;

import java.util.List;
import java.util.Scanner;

public final class Main {
    private static final String[] LABS = {
            "process", "threads", "pools", "communication", "scheduling", "sync",
            "deadlocks", "memory", "filesystem", "io", "security", "notes", "all", "self-test"
    };

    private Main() { }

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            runLab(args[0].toLowerCase());
            return;
        }
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();
                System.out.print("Choose a lab (or 0 to exit): ");
                if (!scanner.hasNextLine()) {
                    return;
                }
                String selection = scanner.nextLine().trim().toLowerCase();
                if (selection.equals("0") || selection.equals("exit")) {
                    return;
                }
                try {
                    runLab(selection);
                } catch (IllegalArgumentException | IllegalStateException exception) {
                    System.out.println("Lab error: " + exception.getMessage());
                }
                System.out.println();
            }
        }
    }

    private static void printMenu() {
        System.out.println("""

                === Operating Systems Concepts Lab ===
                1. process       2. threads       3. pools
                4. communication 5. scheduling    6. sync
                7. deadlocks     8. memory        9. filesystem
                10. io           11. security     12. notes
                13. all          14. self-test    0. exit
                """);
    }

    private static void runLab(String lab) throws Exception {
        if (lab.matches("\\d+")) {
            int index = Integer.parseInt(lab);
            if (index < 1 || index > LABS.length) {
                throw new IllegalArgumentException("Choose a number from 1 to " + LABS.length);
            }
            lab = LABS[index - 1];
        }
        switch (lab) {
            case "process" -> processDemo();
            case "threads" -> threadDemo();
            case "pools" -> ResourcePool.runDemo();
            case "communication" -> CommunicationLab.runDemo();
            case "scheduling" -> schedulingDemo();
            case "sync" -> synchronizationDemo();
            case "deadlocks" -> DeadlockLab.runDemo();
            case "memory" -> MemoryManager.runDemo();
            case "filesystem" -> VirtualFileSystem.runDemo();
            case "io" -> IoSystem.runDemo();
            case "security" -> SecurityLab.runDemo();
            case "notes" -> System.out.println("Read the concept-specific files in the notes/ directory.");
            case "all" -> runAll();
            case "self-test" -> SelfTest.run();
            case "help", "--help", "-h" -> printMenu();
            default -> throw new IllegalArgumentException("Unknown lab '" + lab
                    + "'. Available labs: " + String.join(", ", LABS));
        }
    }

    private static void processDemo() {
        ProcessManager manager = new ProcessManager();
        ProcessControlBlock parent = manager.create("shell");
        ProcessControlBlock worker = manager.createChild(parent.processId(), "worker");
        ProcessControlBlock logger = manager.createChild(parent.processId(), "logger");
        manager.dispatch(parent.processId());
        manager.dispatch(worker.processId());
        manager.terminate(worker.processId());
        manager.reap(parent.processId(), worker.processId());
        manager.terminate(parent.processId());
        System.out.println("Process table (logger becomes an orphan when its parent exits):");
        manager.processes().forEach(System.out::println);
        manager.terminate(logger.processId());
        System.out.println("After cleaning up the orphan:");
        manager.processes().forEach(System.out::println);
        System.out.println("A dispatch from shell to worker demonstrates a context switch.");
    }

    private static void threadDemo() {
        ThreadManager manager = new ThreadManager();
        ThreadManager.SimulatedThread userThread =
                manager.create(1, "user-worker", ThreadManager.Level.USER);
        ThreadManager.SimulatedThread kernelThread =
                manager.create(1, "kernel-worker", ThreadManager.Level.KERNEL);
        manager.start(userThread.threadId());
        manager.block(userThread.threadId());
        manager.start(kernelThread.threadId());
        manager.terminate(kernelThread.threadId());
        manager.wake(userThread.threadId());
        manager.threads().forEach(System.out::println);
    }

    private static void schedulingDemo() {
        SchedulingLab scheduler = new SchedulingLab();
        List<SchedulingLab.Job> jobs = SchedulingLab.sampleJobs();
        for (SchedulingLab.Algorithm algorithm : SchedulingLab.Algorithm.values()) {
            SchedulingLab.ScheduleResult result = scheduler.run(jobs, algorithm, 2);
            System.out.println("\n" + algorithm + " | timeline: " + result.timeline());
            System.out.printf("Context switches: %d | average wait: %.2f | average turnaround: %.2f%n",
                    result.contextSwitches(), result.averageWaitingTime(), result.averageTurnaroundTime());
            result.jobs().forEach(System.out::println);
        }
        System.out.println("\nAll schedules use a dispatcher model; SRTF, RR, MLQ, and MLFQ can preempt.");
    }

    private static void synchronizationDemo() throws InterruptedException {
        int[] counter = {0};
        Object mutex = new Object();
        Thread first = new Thread(() -> increment(counter, mutex), "producer");
        Thread second = new Thread(() -> increment(counter, mutex), "consumer");
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println("Mutex-protected counter after 20,000 increments: " + counter[0]);
    }

    private static void increment(int[] counter, Object mutex) {
        for (int i = 0; i < 10_000; i++) {
            synchronized (mutex) {
                counter[0]++;
            }
        }
    }

    private static void runAll() throws Exception {
        processDemo();
        threadDemo();
        ResourcePool.runDemo();
        CommunicationLab.runDemo();
        schedulingDemo();
        synchronizationDemo();
        DeadlockLab.runDemo();
        MemoryManager.runDemo();
        VirtualFileSystem.runDemo();
        IoSystem.runDemo();
        SecurityLab.runDemo();
    }
}
