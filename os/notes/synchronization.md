# Synchronization

Synchronization coordinates concurrent access to shared data. A critical
section must protect invariants from races; mutexes, semaphores, monitors, and
condition variables are standard tools. The `sync` lab uses Java's intrinsic
monitor (`synchronized`) to protect a shared counter.
