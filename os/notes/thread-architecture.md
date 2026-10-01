# Thread architecture

A thread is an execution context, usually containing a program counter,
register state, stack, and thread-local data. Threads in one process share its
address space and many resources. Thread management may be provided by a
runtime, the kernel, or both. Java's native `Thread` API is used in the lab's
mutex and worker-pool examples.
