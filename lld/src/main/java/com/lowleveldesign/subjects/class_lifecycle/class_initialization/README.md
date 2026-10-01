# Class Initialization

Static field initializers and static initializer blocks run once when the JVM
initializes a class, in textual order. Initialization is synchronized by the
JVM.

**Try:** Use a static initializer to set a constant derived from configuration,
then consider whether a method would be clearer.
