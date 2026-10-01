# Reachability and Garbage Collection

An object is eligible for collection when it is no longer reachable from live
program roots. Collection timing is nondeterministic; it is not a resource
cleanup mechanism.

**Try:** Explain why calling `System.gc()` cannot guarantee that an object is
collected immediately.
