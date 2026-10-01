# Process pool

A process pool keeps a bounded number of worker processes and assigns tasks to
them, avoiding repeated process creation and limiting resource usage. Workers
normally have process isolation, unlike threads sharing one address space.
The project's `pools` lab uses a Java executor to demonstrate bounded workers;
it is not an OS-process pool.
