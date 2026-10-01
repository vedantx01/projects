# Operating Systems Concepts Lab

A small, dependency-free Java console project for learning core operating-system
ideas through runnable simulations and focused notes. It models concepts for
teaching; it does not control the host operating system.

## Requirements and running

- JDK 17 or later
- Maven (optional; direct `javac` commands are also provided)

From the project root:

```text
mvn package
java -jar target/operating-systems-lab-1.0.0.jar
```

Or compile and run without Maven:

```text
javac -d out src/main/java/edu/oslab/*.java
java -cp out edu.oslab.Main
```

The interactive menu runs individual labs. You can also pass a lab name:

```text
java -cp out edu.oslab.Main scheduling
java -cp out edu.oslab.Main all
java -cp out edu.oslab.Main self-test
```

## Syllabus map

| Syllabus area | Where to explore |
| --- | --- |
| Processes, PCB, states, lifecycle, parent/child, zombies/orphans, context switches | `process` lab; `ProcessManager`, `ProcessControlBlock`; dedicated files such as `notes/pcb.md` |
| Threads, lifecycle, multiple threads, user/kernel threads | `threads` lab; `ThreadManager`; dedicated files such as `notes/thread-types.md` |
| Process/thread architecture, pools | `pools` lab; `notes/process-pool.md`, `notes/thread-pool.md` |
| P2P, P2T, T2P, T2T communication | `communication` lab; one direction per file in `notes/` |
| Scheduling queues, dispatcher, preemption, FCFS, SJF, SRTF, priority, RR, MLQ, MLFQ | `scheduling` lab; `SchedulingLab`; scheduling notes in `notes/` |
| Synchronization and deadlocks | `sync`, `deadlocks` labs; `DeadlockLab`; `notes/synchronization.md`, `notes/deadlocks.md` |
| Memory management | `memory` lab; `MemoryManager`; `notes/memory-management.md` |
| File systems and I/O | `filesystem`, `io` labs; `VirtualFileSystem`, `IoSystem`; dedicated notes in `notes/` |
| Access control, authentication/authorization, encryption | `security` lab; `SecurityLab`; dedicated notes in `notes/` |
| Distributed systems, virtualization, hypervisors, containers, cloud OS, multiprocessors, NUMA, SMP | One concept per note in `notes/` |
| Batch, multiprogramming, multitasking, time-sharing, distributed, network, real-time OS | One OS type per note in `notes/` |

## Lab boundaries

The file system is in-memory, the I/O devices are simulated, and process/thread
objects are teaching models. The security lab demonstrates standard JDK
cryptographic APIs with demo-only credentials and keys; do not use its sample
configuration for production secrets.
