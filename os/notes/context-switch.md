# Context switch

A context switch changes the CPU from one process or thread to another. The OS
saves the outgoing execution state, selects a runnable task, and restores its
state. Switching enables concurrency but has overhead and usually performs no
application work. A process switch may require address-space changes; a thread
switch within one process can often retain the same address space.
