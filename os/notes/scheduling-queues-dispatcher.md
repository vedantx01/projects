# Scheduling queues and dispatcher

Schedulers organize runnable work in ready queues and select the next task.
The dispatcher performs the handoff by switching execution context and
starting the selected process or thread. The scheduling lab represents this
with ready-job selection, timeline segments, and context-switch counts.
