# Singleton

Ensure a type has one shared instance and a controlled access point. In Java,
an enum singleton is simple and handles serialization safely; avoid using a
singleton where dependency injection would make scope clearer.

**Try:** Implement an enum-backed application configuration and test that all
callers see the same instance.
