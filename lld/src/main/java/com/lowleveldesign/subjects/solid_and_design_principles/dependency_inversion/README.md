# Dependency Inversion Principle

High-level policy and low-level details should depend on stable abstractions.
Inject collaborators so the caller chooses the implementation.

**Try:** Pass a repository interface into a service instead of constructing a
database client inside the service.
