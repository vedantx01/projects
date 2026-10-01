# User-level and kernel-level threads

**User-level threads** are managed by a user-space runtime or library. Their
management can be fast, but blocking operations or runtime limitations can
constrain concurrency. **Kernel-level threads** are known to the OS scheduler
and can run in parallel on multiple CPUs, with kernel management overhead.
Modern runtimes may use hybrid mappings.
