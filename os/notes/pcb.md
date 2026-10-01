# Process control block

A process control block (PCB) is the operating system's record for a process.
Typical fields include the process identifier, state, saved CPU registers,
program counter, scheduling data, memory mappings, open resources, and
accounting information. The lab's `ProcessControlBlock` is a deliberately
small teaching example rather than a complete kernel structure.
