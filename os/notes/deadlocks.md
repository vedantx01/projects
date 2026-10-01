# Deadlocks

A deadlock occurs when a set of processes or threads wait indefinitely for
resources held by one another. The four Coffman conditions are mutual
exclusion, hold-and-wait, no preemption, and circular wait. Prevention breaks
at least one condition; avoidance checks for safe allocations; detection finds
deadlock patterns; recovery reclaims resources or terminates participants.
The `deadlocks` lab detects cycles in a wait-for graph.
