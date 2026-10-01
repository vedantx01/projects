# Coupling

Coupling measures how much one module knows about another. Small, stable
interfaces and dependency injection reduce unnecessary coupling.

**Try:** Replace a direct dependency on a concrete mail provider with an
interface and inject a test implementation.
