# Thread-to-thread communication (T2T)

T2T passes information between threads. Threads in one process can share
memory, but concurrent access requires synchronization; queues and channels
are alternatives. The communication lab represents each thread as an addressed
endpoint with its own mailbox.
