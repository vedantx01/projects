# Constructor Overloading

Constructors can provide multiple valid ways to create an instance. Prefer
constructor delegation or named factories when overloads otherwise become
ambiguous.

**Try:** Implement `Animal()`, `Animal(String type)`, and
`Animal(String type, int age)` while validating the inputs.
