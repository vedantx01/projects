# Thread pool

A thread pool creates a bounded set of worker threads that repeatedly take
tasks from a queue. Pooling reduces creation overhead and provides backpressure
or a rejection policy when overloaded. The `pools` lab uses Java's
`ThreadPoolExecutor` and a bounded work queue.
