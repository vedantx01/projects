# Read-Write Lock

A read-write lock can allow concurrent readers while ensuring writers are
exclusive. Use it only when the workload benefits from the added complexity.

**Try:** Protect a read-heavy in-memory catalog and measure contention.
