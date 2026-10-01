# RAII and Resource Management

Java uses `AutoCloseable` and try-with-resources to deterministically release
resources, rather than relying on garbage collection.

**Try:** Wrap a resource in `AutoCloseable` and verify it closes when an
exception is thrown.
