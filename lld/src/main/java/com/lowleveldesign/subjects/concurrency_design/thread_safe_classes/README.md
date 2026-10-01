# Thread-Safe Class Design

A thread-safe class preserves its invariants under concurrent calls. Minimize
shared mutable state and document synchronization guarantees.

**Try:** Make a counter safe with an atomic value, then compare with locking.
