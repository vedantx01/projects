# Process states

- **New:** process creation is being prepared.
- **Ready:** able to run, waiting for CPU time.
- **Running:** currently executing on a CPU.
- **Waiting/blocked:** waiting for an event, commonly I/O completion.
- **Terminated:** execution has ended and resources are being reclaimed.
- **Zombie:** exited, but its parent has not collected its exit status.

An OS moves processes among states as the dispatcher grants CPU time and
processes wait for events. State names and exact transitions vary by system.
