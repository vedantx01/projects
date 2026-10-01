# Preemptive and non-preemptive scheduling

In non-preemptive scheduling, a running task keeps the CPU until it finishes
or blocks. Preemptive scheduling can interrupt it, for example when its time
quantum expires or a higher-priority task becomes ready. Preemption can improve
responsiveness but adds context-switch and synchronization overhead.
