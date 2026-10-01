# Producer-Consumer

The demo uses a bounded `BlockingQueue` to coordinate producers and consumers
without manual wait/notify. A bounded buffer provides backpressure.

**Try:** Extend the demo to process multiple messages and shut down cleanly.
