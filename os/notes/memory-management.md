# Memory management

Memory management tracks allocation, protection, and translation between
virtual and physical addresses. Paging divides memory into fixed-size pages
and frames; segmentation uses logical regions; contiguous allocation can
fragment free space. The Java `MemoryManager` demonstrates first-fit contiguous
allocation and coalescing adjacent free blocks. It does not implement virtual
memory or page replacement.
