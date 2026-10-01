# Locks

Explicit locks provide synchronization and features such as timed acquisition.
Release them in `finally` blocks, or use structured helpers where possible.

**Try:** Protect a compound account transfer with a `ReentrantLock`.
