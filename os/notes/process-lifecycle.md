# Process lifecycle and child creation

An OS creates a process record, establishes its address space and resources,
and places it in the scheduling system. A process may create child processes;
parent-child relationships support ownership, waiting, and cleanup. On
termination, resources are released and the parent may collect an exit status.
The process lab models child creation, zombie retention, and reaping.

Multiprogramming keeps several processes available so the CPU can switch to
another process while one waits, improving utilization.
