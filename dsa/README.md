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

## Compile and run

Requires a JDK. From the project root in PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem src\dsa -Recurse -Filter *.java | ForEach-Object FullName)
java -cp out dsa.array.Main
```

All structures are integer-oriented unless their domain naturally uses characters/strings. Methods document behavior through names and validation; invalid indices, capacities, or unsupported input are reported with standard Java exceptions. The XOR-list exercise stores XOR links as integer indices rather than trying to XOR JVM object references. The LFU cache is a straightforward array-backed reference implementation; its lookup/eviction is linear in capacity.
