package edu.oslab;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ThreadManager {
    public enum State {
        NEW,
        READY,
        RUNNING,
        BLOCKED,
        TERMINATED
    }

    public enum Level {
        USER,
        KERNEL
    }

    public static final class SimulatedThread {
        private final int threadId;
        private final int processId;
        private final String name;
        private final Level level;
        private State state = State.READY;

        private SimulatedThread(int threadId, int processId, String name, Level level) {
            this.threadId = threadId;
            this.processId = processId;
            this.name = name;
            this.level = level;
        }

        public int threadId() { return threadId; }
        public int processId() { return processId; }
        public String name() { return name; }
        public Level level() { return level; }
        public State state() { return state; }

        @Override
        public String toString() {
            return "TID=%d PID=%d name=%s level=%s state=%s".formatted(
                    threadId, processId, name, level, state);
        }
    }

    private final Map<Integer, SimulatedThread> threads = new LinkedHashMap<>();
    private int nextThreadId = 1;

    public SimulatedThread create(int processId, String name, Level level) {
        if (processId < 1 || name == null || name.isBlank() || level == null) {
            throw new IllegalArgumentException("A valid process, name, and thread level are required");
        }
        SimulatedThread thread = new SimulatedThread(nextThreadId++, processId, name, level);
        threads.put(thread.threadId(), thread);
        return thread;
    }

    public void start(int threadId) {
        SimulatedThread thread = requireThread(threadId);
        if (thread.state != State.READY) {
            throw new IllegalStateException("Only a ready thread can start");
        }
        thread.state = State.RUNNING;
    }

    public void block(int threadId) {
        SimulatedThread thread = requireThread(threadId);
        if (thread.state != State.RUNNING) {
            throw new IllegalStateException("Only a running thread can block");
        }
        thread.state = State.BLOCKED;
    }

    public void wake(int threadId) {
        SimulatedThread thread = requireThread(threadId);
        if (thread.state != State.BLOCKED) {
            throw new IllegalStateException("Only a blocked thread can wake");
        }
        thread.state = State.READY;
    }

    public void terminate(int threadId) {
        SimulatedThread thread = requireThread(threadId);
        if (thread.state == State.TERMINATED) {
            throw new IllegalStateException("Thread has already terminated");
        }
        thread.state = State.TERMINATED;
    }

    public List<SimulatedThread> threads() {
        return List.copyOf(threads.values());
    }

    private SimulatedThread requireThread(int threadId) {
        SimulatedThread thread = threads.get(threadId);
        if (thread == null) {
            throw new IllegalArgumentException("Unknown thread ID: " + threadId);
        }
        return thread;
    }
}
