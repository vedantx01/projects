package dsa.array;

import dsa.graph.Graph;
import dsa.graph.GraphAlgorithms;
import dsa.linked_list.DoublyLinkedListInt;
import dsa.linked_list.LinkedListAlgorithms;
import dsa.linked_list.LinkedListRepresentations;
import dsa.linked_list.LinkedListStructures;
import dsa.linked_list.SinglyLinkedListInt;
import dsa.tree.AvlTreeInt;
import dsa.tree.BinarySearchTreeInt;
import dsa.tree.Trie;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        DynamicArrayInt dynamicArray = new DynamicArrayInt();
        dynamicArray.add(4);
        dynamicArray.insert(0, 2);
        checkArray(new int[] {2, 4}, dynamicArray.toArray(), "dynamic array");

        SinglyLinkedListInt list = new SinglyLinkedListInt();
        list.addLast(1);
        list.addLast(2);
        list.reverse();
        checkArray(new int[] {2, 1}, list.toArray(), "linked list");

        HashTableInt table = new HashTableInt(2);
        for (int i = 0; i < 100; i++) table.put(i, i * 10);
        table.remove(42);
        check(table.getOrDefault(42, -1) == -1 && table.get(99) == 990, "hash table");

        BinarySearchTreeInt tree = new BinarySearchTreeInt();
        for (int value : new int[] {5, 2, 8, 1, 3}) tree.insert(value);
        tree.remove(5);
        checkArray(new int[] {1, 2, 3, 8}, tree.inOrder(), "binary search tree");

        AvlTreeInt avl = new AvlTreeInt();
        for (int value = 0; value < 100; value++) avl.insert(value);
        check(avl.height() < 10 && avl.contains(50), "AVL tree");

        Trie trie = new Trie();
        trie.insert("algorithm");
        trie.insert("algo");
        check(trie.contains("algorithm") && trie.startsWith("alg") && trie.remove("algo"), "trie");

        FenwickTree fenwick = new FenwickTree(new int[] {2, 4, 6, 8});
        check(fenwick.rangeSum(1, 4) == 18, "Fenwick tree");
        SegmentTree segmentTree = new SegmentTree(new int[] {2, 4, 6, 8});
        segmentTree.update(1, 10);
        check(segmentTree.rangeSum(1, 3) == 16, "segment tree");

        int[] sample = {5, -1, 3, 3, 0, Integer.MIN_VALUE, Integer.MAX_VALUE};
        int[] expected = {Integer.MIN_VALUE, -1, 0, 3, 3, 5, Integer.MAX_VALUE};
        int[][] sortingSamples = new int[7][];
        for (int i = 0; i < sortingSamples.length; i++) sortingSamples[i] = copy(sample);
        SortingAlgorithms.bubbleSort(sortingSamples[0]);
        SortingAlgorithms.insertionSort(sortingSamples[1]);
        SortingAlgorithms.mergeSort(sortingSamples[2]);
        SortingAlgorithms.quickSort(sortingSamples[3]);
        SortingAlgorithms.heapSort(sortingSamples[4]);
        SortingAlgorithms.shellSort(sortingSamples[5]);
        SortingAlgorithms.selectionSort(sortingSamples[6]);
        for (int[] sorted : sortingSamples) checkArray(expected, sorted, "sorting");
        int[] smallValues = {3, -2, 3, 0, -2, 8};
        int[] smallExpected = {-2, -2, 0, 3, 3, 8};
        int[] counted = copy(smallValues);
        SortingAlgorithms.countingSort(counted);
        checkArray(smallExpected, counted, "counting sort");
        int[] radix = {170, 45, 75, 90, 802, 24, 2, 66};
        SortingAlgorithms.radixSortNonNegative(radix);
        checkArray(new int[] {2, 24, 45, 66, 75, 90, 170, 802}, radix, "radix sort");
        check(SearchingAlgorithms.binarySearch(expected, 3) >= 0
            && SearchingAlgorithms.lowerBound(expected, 3) == 3, "searching");

        checkArray(new int[] {0, 2}, StringAlgorithms.kmpSearchAll("ababa", "aba"), "KMP");
        check(StringAlgorithms.editDistance("kitten", "sitting") == 3, "edit distance");
        check(DynamicProgramming.longestIncreasingSubsequenceLength(new int[] {3, 1, 2, 5, 4}) == 3, "LIS");
        check(DynamicProgramming.minimumCoinCount(new int[] {1, 3, 4}, 6) == 2, "coin change");
        check(BacktrackingAlgorithms.countNQueensSolutions(4) == 2, "N-Queens");
        check(BacktrackingAlgorithms.subsetSumExists(new int[] {2, 7, 11}, 9), "subset sum");

        checkArray(new int[] {3, 4, 1, 2}, rotated(new int[] {1, 2, 3, 4}, 2), "array rotation");
        checkArray(new int[] {2, 1}, ArrayAlgorithms.removeDuplicates(new int[] {2, 1, 2, 1}), "array unique");
        check(ArrayAlgorithms.rangeSum(ArrayAlgorithms.prefixSums(new int[] {2, 4, 6, 8}), 1, 3) == 18,
            "prefix/range sum");
        int[] differences = ArrayAlgorithms.differenceArray(new int[] {1, 2, 3, 4});
        ArrayAlgorithms.addRange(differences, 1, 2, 5);
        checkArray(new int[] {1, 7, 8, 4}, ArrayAlgorithms.restoreFromDifference(differences), "difference array");
        check(ArrayAlgorithms.equilibriumIndex(new int[] {1, 3, 5, 2, 2}) == 2, "equilibrium index");
        checkLongArray(new long[] {24, 12, 8, 6}, ArrayAlgorithms.productExceptSelf(new int[] {1, 2, 3, 4}),
            "product except self");
        check(ArrayAlgorithms.countSubarraysWithSum(new int[] {1, 1, 1}, 2) == 2, "subarray sum count");
        check(ArrayAlgorithms.maximumSubarraySum(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6,
            "Kadane maximum sum");
        check(ArrayAlgorithms.maximumProductSubarray(new int[] {2, 3, -2, 4}) == 6, "maximum product");
        checkArray(new int[] {1, 3, 6, 10}, ArrayAlgorithms.runningSum(new int[] {1, 2, 3, 4}), "running sum");
        checkArray(new int[] {0, 1}, ArrayAlgorithms.twoSumSorted(new int[] {2, 7, 11}, 9), "two sum");
        checkArray(new int[] {0, 1}, ArrayAlgorithms.twoSum(new int[] {2, 7, 11}, 9), "hash two sum");
        check(ArrayAlgorithms.countSubarraysWithSum(
            new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, -Integer.MAX_VALUE}, Integer.MAX_VALUE) == 3,
            "wide prefix sum count");
        check(ArrayAlgorithms.threeSum(new int[] {-1, 0, 1, 2, -1, -4}, 0).length == 2, "three sum");
        check(ArrayAlgorithms.fourSum(new int[] {1, 0, -1, 0, -2, 2}, 0).length == 3, "four sum");
        check(ArrayAlgorithms.hasPairWithDifference(new int[] {1, 5, 3}, 2), "pair difference");
        check(ArrayAlgorithms.maxContainerArea(new int[] {1, 8, 6, 2, 5, 4, 8, 3, 7}) == 49, "container area");
        check(ArrayAlgorithms.trappedRainWater(new int[] {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}) == 6,
            "trapped rainwater");
        checkArray(new int[] {3, 3, 5, 5, 6, 7},
            ArrayAlgorithms.slidingWindowMaximum(new int[] {1, 3, -1, -3, 5, 3, 6, 7}, 3), "sliding maximum");
        check(ArrayAlgorithms.longestSubarrayWithSum(new int[] {10, 5, 2, 7, 1, 9}, 15) == 4,
            "longest sum subarray");
        check(ArrayAlgorithms.minimumSubarrayLengthAtLeast(new int[] {2, 3, 1, 2, 4, 3}, 7) == 2,
            "minimum length subarray");
        check(ArrayAlgorithms.longestUniqueSubarray(new int[] {1, 2, 1, 3, 4, 3}) == 4, "longest unique");
        int[] colors = {2, 0, 2, 1, 1, 0};
        ArrayAlgorithms.sort012(colors);
        checkArray(new int[] {0, 0, 1, 1, 2, 2}, colors, "Dutch flag");
        check(ArrayAlgorithms.intersection(new int[] {1, 2, 2}, new int[] {2, 3})[0] == 2, "array intersection");
        check(ArrayAlgorithms.union(new int[] {1, 2}, new int[] {2, 3}).length == 3, "array union");
        check(ArrayAlgorithms.kthSmallest(new int[] {7, 10, 4, 3, 20, 15}, 3) == 7, "kth smallest");
        check(ArrayAlgorithms.kthLargest(new int[] {7, 10, 4, 3, 20, 15}, 2) == 15, "kth largest");
        check(ArrayAlgorithms.majorityElement(new int[] {2, 2, 1, 2}) == 2, "majority");
        check(ArrayAlgorithms.countInversions(new int[] {2, 4, 1, 3, 5}) == 3, "inversion count");
        check(ArrayAlgorithms.hasZeroSumSubarray(new int[] {4, 2, -3, 1, 6}), "zero sum subarray");
        check(ArrayAlgorithms.countSubarraysDivisibleByK(new int[] {4, 5, 0, -2, -3, 1}, 5) == 7,
            "subarray divisible");
        check(ArrayAlgorithms.longestIncreasingContiguousSubarray(new int[] {1, 2, 4, 3}) == 3,
            "increasing contiguous");
        check(ArrayAlgorithms.longestDecreasingContiguousSubarray(new int[] {5, 4, 3, 6}) == 3,
            "decreasing contiguous");
        check(ArrayAlgorithms.longestAlternatingSubarray(new int[] {1, 2, 3, 4}) == 4, "alternating contiguous");
        check(ArrayAlgorithms.maximumCircularSubarraySum(new int[] {5, -3, 5}) == 10, "circular max sum");
        check(ArrayAlgorithms.maximumDifferenceLaterMinusEarlier(new int[] {2, 3, 10, 6, 4, 8, 1}) == 8,
            "maximum difference");
        check(ArrayAlgorithms.maximumStockProfitOneTransaction(new int[] {7, 1, 5, 3, 6, 4}) == 5,
            "stock one transaction");
        check(ArrayAlgorithms.maximumStockProfitUnlimitedTransactions(new int[] {7, 1, 5, 3, 6, 4}) == 7,
            "stock multiple transactions");
        check(MatrixAlgorithms.spiralTraversal(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}).length == 9,
            "spiral matrix");
        checkArray(new int[] {2, 1}, MatrixAlgorithms.searchSortedMatrix(
            new int[][] {{1, 4, 7}, {2, 5, 8}, {3, 6, 9}}, 6), "sorted matrix search");
        check(MatrixAlgorithms.rectangleSum(MatrixAlgorithms.prefixSum2D(new int[][] {{1, 2}, {3, 4}}), 0, 0, 1, 1) == 10,
            "2D prefix sums");

        LinkedListAlgorithms.Node linked = null;
        for (int value : new int[] {1, 2, 3, 4, 5}) linked = LinkedListAlgorithms.insertEnd(linked, value);
        linked = LinkedListAlgorithms.reverseKGroup(linked, 2);
        checkArray(new int[] {2, 1, 4, 3, 5}, LinkedListAlgorithms.traverse(linked), "k-group reverse");
        linked = LinkedListAlgorithms.reorder(linked);
        check(LinkedListAlgorithms.length(linked) == 5, "linked reorder");
        LinkedListAlgorithms.Node cycle = LinkedListAlgorithms.makeCircular(
            LinkedListAlgorithms.insertEnd(LinkedListAlgorithms.insertEnd(null, 1), 2));
        check(LinkedListAlgorithms.cycleLength(cycle) == 2, "cycle length");
        LinkedListAlgorithms.removeCycle(cycle);
        check(!LinkedListAlgorithms.hasCycle(cycle), "cycle removal");
        LinkedListStructures.LruCache lru = new LinkedListStructures.LruCache(2);
        lru.put(1, 10);
        lru.put(2, 20);
        check(lru.get(1) == 10, "LRU read");
        lru.put(3, 30);
        check(lru.get(2) == -1 && lru.get(3) == 30, "LRU eviction");
        LinkedListStructures.TextEditor editor = new LinkedListStructures.TextEditor();
        editor.insert("ac");
        editor.moveLeft(1);
        editor.insert("b");
        check("abc".equals(editor.toString()), "linked text editor");
        LinkedListStructures.CircularQueue linkedQueue = new LinkedListStructures.CircularQueue();
        linkedQueue.offer(1);
        linkedQueue.offer(2);
        check(linkedQueue.poll() == 1 && linkedQueue.peek() == 2, "linked circular queue");
        LinkedListStructures.LfuCache lfu = new LinkedListStructures.LfuCache(2);
        lfu.put(1, 10);
        lfu.put(2, 20);
        check(lfu.get(1) == 10, "LFU read");
        lfu.put(3, 30);
        check(lfu.get(2) == -1 && lfu.get(3) == 30, "LFU eviction");
        LinkedListStructures.MusicPlaylist playlist = new LinkedListStructures.MusicPlaylist();
        playlist.add("one");
        playlist.add("two");
        check("two".equals(playlist.next()) && "one".equals(playlist.next()), "circular playlist");
        LinkedListRepresentations.UnrolledList unrolled = new LinkedListRepresentations.UnrolledList();
        for (int i = 0; i < 20; i++) unrolled.add(unrolled.size(), i);
        checkArray(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19},
            unrolled.toArray(), "unrolled list");
        unrolled.add(8, 99);
        check(unrolled.get(8) == 99 && unrolled.remove(8) == 99, "unrolled list insertion");
        LinkedListRepresentations.XorLinkedList xorList = new LinkedListRepresentations.XorLinkedList();
        xorList.addLast(4);
        xorList.addLast(5);
        xorList.addLast(6);
        checkArray(new int[] {4, 5, 6}, xorList.toArray(), "XOR linked list forward");
        checkArray(new int[] {6, 5, 4}, xorList.toArrayReverse(), "XOR linked list reverse");
        LinkedListRepresentations.SkipList skipList = new LinkedListRepresentations.SkipList();
        skipList.insert(2);
        skipList.insert(1);
        skipList.insert(3);
        check(skipList.contains(2) && skipList.remove(2) && !skipList.contains(2), "skip list");
        LinkedListRepresentations.SparseMatrix sparse = new LinkedListRepresentations.SparseMatrix(3, 3);
        sparse.set(1, 2, 7);
        check(sparse.get(1, 2) == 7 && sparse.nonZeroCount() == 1, "sparse matrix");
        LinkedListRepresentations.Polynomial polynomial = new LinkedListRepresentations.Polynomial();
        polynomial.addTerm(2, 2);
        polynomial.addTerm(1, 0);
        check(polynomial.evaluate(3) == 19, "polynomial list");
        LinkedListRepresentations.AdjacencyList adjacency = new LinkedListRepresentations.AdjacencyList(3, false);
        adjacency.addEdge(0, 1);
        checkArray(new int[] {1}, adjacency.neighbors(0), "linked adjacency list");
        DoublyLinkedListInt doubly = new DoublyLinkedListInt();
        doubly.addLast(1);
        doubly.addLast(3);
        doubly.insert(1, 2);
        checkArray(new int[] {1, 2, 3}, doubly.toArray(), "doubly list insert");
        checkArray(new int[] {3, 2, 1}, doubly.toArrayReverse(), "doubly list reverse traversal");
        check(doubly.removeAt(1) == 2 && doubly.indexOf(3) == 1, "doubly list delete/search");
        int[][] matrixToRotate = {{1, 2}, {3, 4}};
        MatrixAlgorithms.rotateClockwise90(matrixToRotate);
        checkArray(new int[] {3, 1, 4, 2}, MatrixAlgorithms.traverse(matrixToRotate), "matrix rotation");
        LinkedListAlgorithms.Node palindrome = null;
        for (int value : new int[] {1, 2, 3, 2, 1}) palindrome = LinkedListAlgorithms.insertEnd(palindrome, value);
        check(LinkedListAlgorithms.isPalindrome(palindrome), "linked palindrome");
        checkArray(new int[] {1, 2, 3, 2, 1}, LinkedListAlgorithms.traverse(palindrome),
            "palindrome restores list");
        check(LinkedListAlgorithms.recursivePalindrome(palindrome), "recursive linked palindrome");
        check(LinkedListAlgorithms.isHappyNumber(19) && !LinkedListAlgorithms.isHappyNumber(2), "happy number");
        LinkedListAlgorithms.Node recursiveGroups = null;
        for (int value : new int[] {1, 2, 3, 4, 5, 6}) {
            recursiveGroups = LinkedListAlgorithms.insertEnd(recursiveGroups, value);
        }
        recursiveGroups = LinkedListAlgorithms.reverseKGroupRecursive(recursiveGroups, 3);
        checkArray(new int[] {3, 2, 1, 6, 5, 4}, LinkedListAlgorithms.traverse(recursiveGroups),
            "recursive k-group reverse");
        LinkedListStructures.UndoRedoText undoRedo = new LinkedListStructures.UndoRedoText();
        undoRedo.insert("abc");
        check(undoRedo.deleteLast() && "ab".equals(undoRedo.text()), "undo-redo delete");
        check(undoRedo.undo() && "abc".equals(undoRedo.text()), "undo");
        check(undoRedo.redo() && "ab".equals(undoRedo.text()), "redo");
        LinkedListAlgorithms.MultiLevelNode multiLevel = new LinkedListAlgorithms.MultiLevelNode(1);
        multiLevel.next = new LinkedListAlgorithms.MultiLevelNode(4);
        multiLevel.child = new LinkedListAlgorithms.MultiLevelNode(2);
        multiLevel.child.next = new LinkedListAlgorithms.MultiLevelNode(3);
        check(multiLevel == LinkedListAlgorithms.flattenMultilevel(multiLevel)
            && multiLevel.next.value == 2 && multiLevel.next.next.value == 3
            && multiLevel.next.next.next.value == 4, "multilevel flatten");

        Graph graph = new Graph(4, false);
        graph.addEdge(0, 1, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);
        graph.addEdge(0, 3, 10);
        checkArray(new int[] {0, 4, 6, 9}, GraphAlgorithms.dijkstra(graph, 0), "Dijkstra");
        check(GraphAlgorithms.primMstWeight(graph) == 9, "Prim");
        check(GraphAlgorithms.kruskalMstWeight(graph) == 9, "Kruskal");
        Graph wideEdge = new Graph(2, false);
        wideEdge.addEdge(0, 1, Integer.MAX_VALUE);
        check(GraphAlgorithms.primMstWeight(wideEdge) == Integer.MAX_VALUE, "Prim maximum edge weight");

        Graph directed = new Graph(3, true);
        directed.addEdge(0, 1, 5);
        directed.addEdge(1, 2, -2);
        checkArray(new int[] {0, 1, 2}, GraphAlgorithms.topologicalSort(directed), "topological sort");
        checkArray(new int[] {0, 5, 3}, GraphAlgorithms.bellmanFord(directed, 0), "Bellman-Ford");
        check(GraphAlgorithms.floydWarshall(directed)[0][2] == 3
            && !GraphAlgorithms.hasCycle(directed), "Floyd-Warshall and cycle detection");

        System.out.println("All data structure and algorithm smoke tests passed.");
    }

    private static int[] copy(int[] source) {
        int[] result = new int[source.length];
        for (int i = 0; i < source.length; i++) result[i] = source[i];
        return result;
    }

    private static int[] rotated(int[] values, int distance) {
        ArrayAlgorithms.rotateLeft(values, distance);
        return values;
    }

    private static void checkLongArray(long[] expected, long[] actual, String name) {
        if (expected.length != actual.length) throw new AssertionError(name + ": length mismatch");
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) throw new AssertionError(name + ": mismatch at " + i);
        }
    }

    private static void checkArray(int[] expected, int[] actual, String name) {
        if (expected.length != actual.length) throw new AssertionError(name + ": length mismatch");
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) throw new AssertionError(name + ": mismatch at " + i);
        }
    }

    private static void check(boolean condition, String name) {
        if (!condition) throw new AssertionError(name + " failed");
    }
}
