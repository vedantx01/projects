# Java Data Structures and Algorithms

A dependency-free collection of foundational data structures and algorithms, implemented with primitive arrays and custom node types. It deliberately does not use Java's collection classes (`ArrayList`, `HashMap`, `Queue`, `Stack`, `PriorityQueue`, and similar).

This is a practical core catalog, not a claim to include every algorithm ever devised. It focuses on common interview, coursework, and systems fundamentals. Additions can follow the same rule: implement the underlying storage rather than delegating to a collection.

## Contents

### Data structures (`src/dsa`)

- `array/`: `DynamicArrayInt`, `ArrayStackInt`, `CircularQueueInt`, `MinHeapInt`, `HashTableInt`, `FenwickTree`, and `SegmentTree`
- `linked_list/`: `SinglyLinkedListInt`, `DoublyLinkedListInt`, and the algorithm/structure catalogs below
- `tree/`: `BinarySearchTreeInt`, `AvlTreeInt`, and `Trie`
- `graph/`: `Graph`, `GraphAlgorithms`, and `DisjointSet`

### Algorithms

- `array/SortingAlgorithms`: bubble, selection, insertion, shell, merge, quick, heap, counting, and radix sort
- `array/SearchingAlgorithms`: linear, binary, lower/upper bound, rotated-array search, and jump search
- `array/StringAlgorithms`: naive search, KMP, Rabin–Karp, palindrome, anagram, and edit distance
- `array/DynamicProgramming`: Fibonacci, LCS, LIS, 0/1 knapsack, and coin change
- `graph/GraphAlgorithms`: BFS, DFS, cycle detection, topological sort, Dijkstra, Bellman–Ford, Floyd–Warshall, Prim, and Kruskal
- `array/BacktrackingAlgorithms`: permutations, N-Queens count, right/down maze path count, and subset-sum decision
- `array/ArrayAlgorithms`: array CRUD/traversal, rotations, frequencies/uniqueness, prefix and difference arrays, range/subarray sums, products, hash/sorted two-sum, three/four-sum, sliding windows, partitioning, order statistics, inversions, stock profit, and contiguous-subarray patterns
- `array/MatrixAlgorithms`: matrix traversal, spiral order, transpose, square-matrix rotation, sorted-matrix search, and 2D prefix/range sums
- `linked_list/LinkedListAlgorithms`: singly/doubly/circular list operations; iterative/recursive, group, range, and alternate reversals; cycle/fast-slow problems; merge/sort/dedup; reorder/rotation/partition; random-pointer copying; multilevel flattening; arithmetic lists; and recursive helpers
- `linked_list/LinkedListStructures`: linked stack, queue, circular queue, deque, priority queue, LRU/LFU caches, browser history, playlist, linked text editor, and DLL-backed undo/redo
- `linked_list/LinkedListRepresentations`: sparse matrix, polynomial, adjacency list, skip list, index-based XOR list, unrolled list, and self-organizing list
- `tree/TreeAlgorithms`: binary-tree construction/serialization, recursive and iterative traversals, level/zigzag/boundary/views, BST operations and order statistics, path/ancestor queries, Morris traversals, shape checks, and path/width/sum metrics
- `graph/GraphAlgorithms`: recursive/iterative DFS, iterative/recursive and multi-source BFS, components, cycle checks, DAG paths, bipartiteness, tree validation, shortest paths, spanning trees, and topological ordering
- `graph/GraphTraversalMetrics`: deterministic visits, neighbor checks, peak worklist/depth, and an explicitly approximate auxiliary-memory model for the four BFS/DFS variants

## Compile and run

Requires a JDK. From the project root in PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem src\dsa -Recurse -Filter *.java | ForEach-Object FullName)
java -cp out dsa.array.Main
```

All structures are integer-oriented unless their domain naturally uses characters/strings. Methods document behavior through names and validation; invalid indices, capacities, or unsupported input are reported with standard Java exceptions. The XOR-list exercise stores XOR links as integer indices rather than trying to XOR JVM object references. The LFU cache is a straightforward array-backed reference implementation; its lookup/eviction is linear in capacity.

### BFS/DFS memory metrics

`GraphTraversalMetrics` runs each traversal and reports visited vertices, neighbor probes, peak pending work, and peak recursive depth. These are operation counters and live-depth/frontier metrics, not wall-clock benchmarks. Memory estimates exclude the graph itself and returned traversal output. The byte model assumes a 4-byte `int`/reference slot, a 1-byte `boolean` slot, and an illustrative 32 bytes per active recursive frame; JVM headers, alignment, compressed references, JIT frame layout, and stack reservation vary by runtime.

For `V` vertices, the matrix graph itself has an approximate payload of `V² × (1 + 4)` bytes. BFS and explicit-stack DFS allocate a `V`-slot work array plus `V` visited flags, or approximately `5V` bytes of auxiliary arrays. Recursive DFS uses `V` visited flags plus recursion frames, approximately `V + 12 + 32D` bytes in this model, where `D` is maximum active DFS depth. Recursive BFS still needs its `V`-slot queue and visited flags, and additionally keeps recursive frames while processing the queue: approximately `5V + 16 + 32R`, where `R` is reached-vertex count. These recursive estimates are illustrative, not portable JVM measurements.

The executable smoke test prints reproducible metrics for a six-vertex chain and star. On the chain, BFS and explicit DFS have a peak frontier of 1; recursive DFS reaches depth 6. On the star, BFS and explicit DFS peak at 5 pending vertices, while recursive DFS reaches depth 2. All four scan 36 neighbor positions on a connected six-vertex graph because this project’s graph uses an adjacency matrix and scans all `V` possible neighbors for every visited vertex.
