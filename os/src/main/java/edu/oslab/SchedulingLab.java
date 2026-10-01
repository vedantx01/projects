package edu.oslab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SchedulingLab {
    public enum Algorithm {
        FCFS,
        SJF,
        SRTF,
        PRIORITY,
        ROUND_ROBIN,
        MULTILEVEL_QUEUE,
        MULTILEVEL_FEEDBACK_QUEUE
    }

    public record Job(String id, int arrivalTime, int burstTime, int priority, int queueLevel) {
        public Job {
            if (id == null || id.isBlank() || arrivalTime < 0 || burstTime < 1
                    || priority < 0 || queueLevel < 0 || queueLevel > 2) {
                throw new IllegalArgumentException("Invalid scheduling job");
            }
        }
    }

    public record JobResult(String id, int completionTime, int turnaroundTime, int waitingTime) { }

    public record ScheduleResult(Algorithm algorithm, List<String> timeline,
                                List<JobResult> jobs, int contextSwitches,
                                double averageWaitingTime, double averageTurnaroundTime) { }

    private static final class RuntimeJob {
        private final Job job;
        private int remaining;
        private int completion = -1;
        private int level;
        private int sliceRemaining;
        private int readyOrder;

        private RuntimeJob(Job job) {
            this.job = job;
            this.remaining = job.burstTime();
            this.level = job.queueLevel();
        }
    }

    public ScheduleResult run(List<Job> jobs, Algorithm algorithm, int quantum) {
        if (jobs == null || jobs.isEmpty() || algorithm == null) {
            throw new IllegalArgumentException("Provide at least one job and an algorithm");
        }
        if (quantum < 1 && algorithm == Algorithm.ROUND_ROBIN) {
            throw new IllegalArgumentException("Round-robin quantum must be positive");
        }
        List<RuntimeJob> runtime = jobs.stream().map(RuntimeJob::new).toList();
        if (algorithm == Algorithm.MULTILEVEL_FEEDBACK_QUEUE) {
            runtime.forEach(task -> task.level = 0);
        }
        List<String> timeline = new ArrayList<>();
        int contextSwitches = algorithm == Algorithm.MULTILEVEL_QUEUE
                || algorithm == Algorithm.MULTILEVEL_FEEDBACK_QUEUE
                || algorithm == Algorithm.SRTF
                || algorithm == Algorithm.ROUND_ROBIN
                ? runPreemptive(runtime, algorithm, quantum, timeline)
                : runNonPreemptive(runtime, algorithm, timeline);

        List<JobResult> results = new ArrayList<>();
        double totalWaiting = 0;
        double totalTurnaround = 0;
        for (RuntimeJob task : runtime) {
            int turnaround = task.completion - task.job.arrivalTime();
            int waiting = turnaround - task.job.burstTime();
            totalWaiting += waiting;
            totalTurnaround += turnaround;
            results.add(new JobResult(task.job.id(), task.completion, turnaround, waiting));
        }
        results.sort(Comparator.comparing(JobResult::id));
        return new ScheduleResult(algorithm, List.copyOf(timeline), List.copyOf(results),
                contextSwitches, totalWaiting / results.size(), totalTurnaround / results.size());
    }

    private int runNonPreemptive(List<RuntimeJob> jobs, Algorithm algorithm, List<String> timeline) {
        Comparator<RuntimeJob> selector = switch (algorithm) {
            case FCFS -> Comparator.comparingInt((RuntimeJob task) -> task.job.arrivalTime());
            case SJF -> Comparator.comparingInt((RuntimeJob task) -> task.job.burstTime())
                    .thenComparingInt(task -> task.job.arrivalTime());
            case PRIORITY -> Comparator.comparingInt((RuntimeJob task) -> task.job.priority())
                    .thenComparingInt(task -> task.job.arrivalTime());
            default -> throw new IllegalArgumentException("Not a non-preemptive algorithm");
        };
        int time = 0;
        int finished = 0;
        String previous = null;
        int switches = 0;
        while (finished < jobs.size()) {
            final int now = time;
            RuntimeJob next = jobs.stream()
                    .filter(task -> task.completion < 0 && task.job.arrivalTime() <= now)
                    .min(selector.thenComparingInt(task -> task.job.arrivalTime())
                            .thenComparing(task -> task.job.id()))
                    .orElse(null);
            if (next == null) {
                time++;
                continue;
            }
            if (previous != null && !previous.equals(next.job.id())) {
                switches++;
            }
            timeline.add("%s[%d-%d]".formatted(next.job.id(), time, time + next.remaining));
            time += next.remaining;
            next.remaining = 0;
            next.completion = time;
            previous = next.job.id();
            finished++;
        }
        return switches;
    }

    private int runPreemptive(List<RuntimeJob> jobs, Algorithm algorithm, int quantum,
                              List<String> timeline) {
        List<RuntimeJob> ready = new ArrayList<>();
        int time = 0;
        int finished = 0;
        int nextReadyOrder = 0;
        int switches = 0;
        String previous = null;
        RuntimeJob active = null;

        while (finished < jobs.size()) {
            for (RuntimeJob task : jobs) {
                if (task.completion < 0 && task.job.arrivalTime() == time && !ready.contains(task)) {
                    task.readyOrder = nextReadyOrder++;
                    ready.add(task);
                }
            }
            if (algorithm == Algorithm.MULTILEVEL_FEEDBACK_QUEUE) {
                promoteWaitingJobs(ready, time);
            }
            if (algorithm == Algorithm.SRTF) {
                active = ready.stream().min(Comparator
                        .comparingInt((RuntimeJob task) -> task.remaining)
                        .thenComparingInt(task -> task.job.arrivalTime())
                        .thenComparing(task -> task.job.id())).orElse(null);
            } else {
                active = ready.stream().min(Comparator
                        .comparingInt((RuntimeJob task) -> queueLevel(task, algorithm))
                        .thenComparingInt(task -> task.readyOrder)
                        .thenComparing(task -> task.job.id())).orElse(null);
            }
            if (active == null) {
                time++;
                continue;
            }
            if (previous != null && !previous.equals(active.job.id())) {
                switches++;
            }
            appendTick(timeline, active.job.id(), time);
            active.remaining--;
            if (algorithm == Algorithm.ROUND_ROBIN) {
                active.sliceRemaining = active.sliceRemaining == 0 ? quantum : active.sliceRemaining;
                active.sliceRemaining--;
            } else if (algorithm == Algorithm.MULTILEVEL_QUEUE
                    || algorithm == Algorithm.MULTILEVEL_FEEDBACK_QUEUE) {
                active.sliceRemaining = active.sliceRemaining == 0
                        ? queueQuantum(active.level) : active.sliceRemaining;
                active.sliceRemaining--;
            }
            time++;
            previous = active.job.id();
            if (active.remaining == 0) {
                active.completion = time;
                ready.remove(active);
                finished++;
            }
            for (RuntimeJob task : jobs) {
                if (task.completion < 0 && task.job.arrivalTime() == time && !ready.contains(task)) {
                    task.readyOrder = nextReadyOrder++;
                    ready.add(task);
                }
            }
            if (active.remaining > 0 && algorithm == Algorithm.ROUND_ROBIN
                    && active.sliceRemaining == 0) {
                active.readyOrder = nextReadyOrder++;
            } else if (active.remaining > 0 && (algorithm == Algorithm.MULTILEVEL_QUEUE
                    || algorithm == Algorithm.MULTILEVEL_FEEDBACK_QUEUE)
                    && active.sliceRemaining == 0) {
                if (algorithm == Algorithm.MULTILEVEL_FEEDBACK_QUEUE) {
                    active.level = Math.min(active.level + 1, 2);
                }
                active.readyOrder = nextReadyOrder++;
            }
        }
        return switches;
    }

    private void promoteWaitingJobs(List<RuntimeJob> ready, int time) {
        for (RuntimeJob task : ready) {
            if (task.level > 0 && time - task.job.arrivalTime() > 0
                    && time % 20 == 0) {
                task.level = 0;
                task.sliceRemaining = 0;
            }
        }
    }

    private int queueLevel(RuntimeJob task, Algorithm algorithm) {
        return switch (algorithm) {
            case MULTILEVEL_QUEUE -> task.job.queueLevel();
            case MULTILEVEL_FEEDBACK_QUEUE -> task.level;
            default -> 0;
        };
    }

    private int queueQuantum(int level) {
        return switch (level) {
            case 0 -> 2;
            case 1 -> 4;
            default -> 8;
        };
    }

    private void appendTick(List<String> timeline, String id, int time) {
        if (!timeline.isEmpty()) {
            int lastIndex = timeline.size() - 1;
            String last = timeline.get(lastIndex);
            int open = last.lastIndexOf('[');
            int dash = last.lastIndexOf('-');
            int close = last.lastIndexOf(']');
            if (last.startsWith(id + "[") && Integer.parseInt(last.substring(dash + 1, close)) == time) {
                timeline.set(lastIndex, id + last.substring(open, dash) + "-"
                        + (time + 1) + "]");
                return;
            }
        }
        timeline.add("%s[%d-%d]".formatted(id, time, time + 1));
    }

    public static List<Job> sampleJobs() {
        return List.of(
                new Job("P1", 0, 7, 2, 0),
                new Job("P2", 2, 4, 1, 1),
                new Job("P3", 4, 1, 3, 2),
                new Job("P4", 5, 4, 2, 1));
    }
}
