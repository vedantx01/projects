# Hash Map Internals

Hash maps use a hash to choose a bucket and equality to distinguish keys within
that bucket. Correct keys must keep `equals` and `hashCode` consistent.

**Try:** Build a small separate-chaining map and test collisions and resizing.
