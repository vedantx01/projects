# Single Responsibility Principle

A class should have one cohesive reason to change. Keep unrelated persistence,
formatting, and business rules in separate collaborators.

**Try:** Split a report class that both calculates totals and writes files.
