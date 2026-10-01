# Process-to-thread communication (P2T)

P2T sends information from a process endpoint to a particular thread endpoint.
Real systems can implement it with shared queues, signals, runtime mailboxes,
or application-defined synchronization. The communication lab models a P2T
message in the destination thread's mailbox.
