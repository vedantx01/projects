package edu.oslab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ProcessManager {
    private final Map<Integer, ProcessControlBlock> processes = new LinkedHashMap<>();
    private int nextProcessId = 1;
    private int currentProcessId = -1;

    public ProcessControlBlock create(String name) {
        return createProcess(null, name);
    }

    public ProcessControlBlock createChild(int parentProcessId, String name) {
        ProcessControlBlock parent = requireProcess(parentProcessId);
        if (parent.state() == ProcessState.TERMINATED || parent.state() == ProcessState.ZOMBIE) {
            throw new IllegalStateException("An exited process cannot create a child");
        }
        return createProcess(parentProcessId, name);
    }

    private ProcessControlBlock createProcess(Integer parentProcessId, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Process name must not be blank");
        }
        ProcessControlBlock pcb = new ProcessControlBlock(nextProcessId++, parentProcessId, name);
        pcb.setState(ProcessState.READY);
        processes.put(pcb.processId(), pcb);
        return pcb;
    }

    public void dispatch(int processId) {
        ProcessControlBlock next = requireProcess(processId);
        if (next.state() != ProcessState.READY && next.state() != ProcessState.RUNNING) {
            throw new IllegalStateException("Only a ready process can be dispatched");
        }
        if (currentProcessId != -1 && currentProcessId != processId) {
            ProcessControlBlock previous = requireProcess(currentProcessId);
            if (previous.state() == ProcessState.RUNNING) {
                previous.setState(ProcessState.READY);
            }
        }
        next.setState(ProcessState.RUNNING);
        currentProcessId = processId;
    }

    public void block(int processId) {
        ProcessControlBlock process = requireProcess(processId);
        if (process.state() != ProcessState.RUNNING && process.state() != ProcessState.READY) {
            throw new IllegalStateException("Only a running or ready process can wait");
        }
        process.setState(ProcessState.WAITING);
        if (currentProcessId == processId) {
            currentProcessId = -1;
        }
    }

    public void wake(int processId) {
        ProcessControlBlock process = requireProcess(processId);
        if (process.state() != ProcessState.WAITING) {
            throw new IllegalStateException("Only a waiting process can be woken");
        }
        process.setState(ProcessState.READY);
    }

    public void terminate(int processId) {
        ProcessControlBlock process = requireProcess(processId);
        if (process.state() == ProcessState.TERMINATED || process.state() == ProcessState.ZOMBIE) {
            throw new IllegalStateException("Process has already exited");
        }
        for (ProcessControlBlock child : processes.values()) {
            if (child.parentProcessId() != null
                    && child.parentProcessId() == processId
                    && child.state() != ProcessState.TERMINATED
                    && child.state() != ProcessState.ZOMBIE) {
                child.setOrphaned(true);
            }
        }
        if (currentProcessId == processId) {
            currentProcessId = -1;
        }
        boolean parentAlive = process.parentProcessId() != null
                && processes.containsKey(process.parentProcessId())
                && processes.get(process.parentProcessId()).state() != ProcessState.TERMINATED
                && processes.get(process.parentProcessId()).state() != ProcessState.ZOMBIE;
        process.setState(parentAlive ? ProcessState.ZOMBIE : ProcessState.TERMINATED);
    }

    public void reap(int parentProcessId, int childProcessId) {
        ProcessControlBlock parent = requireProcess(parentProcessId);
        if (parent.state() == ProcessState.TERMINATED || parent.state() == ProcessState.ZOMBIE) {
            throw new IllegalStateException("An exited parent cannot reap a child");
        }
        ProcessControlBlock child = requireProcess(childProcessId);
        if (child.parentProcessId() == null || child.parentProcessId() != parentProcessId) {
            throw new IllegalArgumentException("The process is not a child of the supplied parent");
        }
        if (child.state() != ProcessState.ZOMBIE) {
            throw new IllegalStateException("Only a zombie child can be reaped");
        }
        child.setState(ProcessState.TERMINATED);
    }

    public Collection<ProcessControlBlock> processes() {
        return List.copyOf(processes.values());
    }

    private ProcessControlBlock requireProcess(int processId) {
        ProcessControlBlock process = processes.get(processId);
        if (process == null) {
            throw new IllegalArgumentException("Unknown process ID: " + processId);
        }
        return process;
    }
}
