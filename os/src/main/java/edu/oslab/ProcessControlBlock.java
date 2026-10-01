package edu.oslab;

public final class ProcessControlBlock {
    private final int processId;
    private final Integer parentProcessId;
    private final String name;
    private ProcessState state;
    private boolean orphaned;

    ProcessControlBlock(int processId, Integer parentProcessId, String name) {
        this.processId = processId;
        this.parentProcessId = parentProcessId;
        this.name = name;
        this.state = ProcessState.NEW;
    }

    public int processId() {
        return processId;
    }

    public Integer parentProcessId() {
        return parentProcessId;
    }

    public String name() {
        return name;
    }

    public ProcessState state() {
        return state;
    }

    public boolean orphaned() {
        return orphaned;
    }

    void setState(ProcessState state) {
        this.state = state;
    }

    void setOrphaned(boolean orphaned) {
        this.orphaned = orphaned;
    }

    @Override
    public String toString() {
        return "PID=%d parent=%s name=%s state=%s%s".formatted(
                processId, parentProcessId == null ? "-" : parentProcessId, name, state,
                orphaned ? " orphan=true" : "");
    }
}
