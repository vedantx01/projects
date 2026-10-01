# Object Usage

Clients use an object through its public methods and preserve its invariants by
avoiding direct access to mutable implementation state.

**Try:** Encapsulate an `Account` balance and expose validated deposit/withdraw
operations instead of a public field.
