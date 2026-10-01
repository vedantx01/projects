# Process architecture

An OS process typically combines an address space, resource ownership, and one
or more execution contexts. The kernel tracks process metadata and mediates
access to CPU, memory, files, and devices. This project demonstrates the ideas
with portable Java records rather than native process APIs.
