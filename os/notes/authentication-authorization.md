# Authentication and authorization

**Authentication** verifies an identity, for example by checking a password
against a salted, slow password hash. **Authorization** evaluates what an
authenticated identity may do. They are separate checks: successful
authentication alone must not grant every permission. The lab uses
PBKDF2-HMAC-SHA-256 for demo credentials and role permissions.
