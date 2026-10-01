# Object Creation

`new` requests an instance after its class is available. The JVM allocates
instance state and initializes fields before the constructor completes.

**Try:** Add overloaded constructors to an `Animal` and delegate them with
`this(...)` so initialization logic stays in one place.
