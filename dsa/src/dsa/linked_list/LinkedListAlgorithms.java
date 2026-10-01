package dsa.linked_list;

public final class LinkedListAlgorithms {
    public static final class Node {
        public int value;
        public Node next;
        public Node(int value) { this.value = value; }
    }

    public static final class DoublyNode {
        public int value;
        public DoublyNode previous;
        public DoublyNode next;
        public DoublyNode(int value) { this.value = value; }
    }

    public static final class RandomNode {
        public int value;
        public RandomNode next;
        public RandomNode random;
        public RandomNode(int value) { this.value = value; }
    }

    public static final class MultiLevelNode {
        public int value;
        public MultiLevelNode next;
        public MultiLevelNode down;
        public MultiLevelNode child;
        public MultiLevelNode(int value) { this.value = value; }
    }

    public static final class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;
        TreeNode(int value) { this.value = value; }
    }

    private LinkedListAlgorithms() { }

    public static int[] traverse(Node head) {
        int[] values = new int[length(head)];
        int index = 0;
        for (Node node = head; node != null; node = node.next) values[index++] = node.value;
        return values;
    }

    public static Node insertBeginning(Node head, int value) {
        Node node = new Node(value);
        node.next = head;
        return node;
    }

    public static Node insertEnd(Node head, int value) {
        Node node = new Node(value);
        if (head == null) return node;
        Node tail = head;
        while (tail.next != null) tail = tail.next;
        tail.next = node;
        return head;
    }

    public static Node insertAt(Node head, int index, int value) {
        if (index < 0) throw new IndexOutOfBoundsException("Index: " + index);
        if (index == 0) return insertBeginning(head, value);
        Node previous = head;
        for (int i = 0; i < index - 1 && previous != null; i++) previous = previous.next;
        if (previous == null) throw new IndexOutOfBoundsException("Index: " + index);
        Node node = new Node(value);
        node.next = previous.next;
        previous.next = node;
        return head;
    }

    public static Node deleteBeginning(Node head) { return head == null ? null : head.next; }

    public static Node deleteEnd(Node head) {
        if (head == null || head.next == null) return null;
        Node node = head;
        while (node.next.next != null) node = node.next;
        node.next = null;
        return head;
    }

    public static Node deleteValue(Node head, int value) {
        if (head == null) return null;
        if (head.value == value) return head.next;
        Node node = head;
        while (node.next != null && node.next.value != value) node = node.next;
        if (node.next != null) node.next = node.next.next;
        return head;
    }

    public static Node deleteAt(Node head, int index) {
        if (index < 0) throw new IndexOutOfBoundsException("Index: " + index);
        if (index == 0) {
            if (head == null) throw new IndexOutOfBoundsException("Index: " + index);
            return head.next;
        }
        Node previous = head;
        for (int i = 0; i < index - 1 && previous != null; i++) previous = previous.next;
        if (previous == null || previous.next == null) throw new IndexOutOfBoundsException("Index: " + index);
        previous.next = previous.next.next;
        return head;
    }

    public static int search(Node head, int target) {
        int index = 0;
        for (Node node = head; node != null; node = node.next, index++) {
            if (node.value == target) return index;
        }
        return -1;
    }

    public static int length(Node head) {
        int count = 0;
        for (Node node = head; node != null; node = node.next) count++;
        return count;
    }

    public static Node reverseIterative(Node head) {
        Node previous = null, current = head;
        while (current != null) {
            Node next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        return previous;
    }

    public static Node reverseRecursive(Node head) {
        if (head == null || head.next == null) return head;
        Node newHead = reverseRecursive(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    public static Node reverseBetween(Node head, int left, int right) {
        if (left < 1 || right < left) throw new IllegalArgumentException("Positions are 1-based and must be ordered");
        Node dummy = new Node(0);
        dummy.next = head;
        Node before = dummy;
        for (int position = 1; position < left; position++) {
            before = before.next;
            if (before == null) throw new IndexOutOfBoundsException("Range exceeds list length");
        }
        Node first = before.next;
        if (first == null) throw new IndexOutOfBoundsException("Range exceeds list length");
        for (int i = 0; i < right - left; i++) {
            if (first.next == null) throw new IndexOutOfBoundsException("Range exceeds list length");
            Node moved = first.next;
            first.next = moved.next;
            moved.next = before.next;
            before.next = moved;
        }
        return dummy.next;
    }

    public static Node reverseKGroup(Node head, int k) {
        if (k <= 0) throw new IllegalArgumentException("k must be positive");
        Node dummy = new Node(0);
        dummy.next = head;
        Node groupPrevious = dummy;
        while (true) {
            Node kth = groupPrevious;
            for (int i = 0; i < k && kth != null; i++) kth = kth.next;
            if (kth == null) break;
            Node groupNext = kth.next, previous = groupNext, current = groupPrevious.next;
            while (current != groupNext) {
                Node next = current.next;
                current.next = previous;
                previous = current;
                current = next;
            }
            Node oldStart = groupPrevious.next;
            groupPrevious.next = kth;
            groupPrevious = oldStart;
        }
        return dummy.next;
    }

    public static Node reverseKGroupRecursive(Node head, int k) {
        if (k <= 0) throw new IllegalArgumentException("k must be positive");
        Node current = head;
        int count = 0;
        while (current != null && count < k) {
            current = current.next;
            count++;
        }
        if (count < k) return head;
        Node previous = null;
        current = head;
        for (int i = 0; i < k; i++) {
            Node next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        head.next = reverseKGroupRecursive(current, k);
        return previous;
    }

    public static Node reverseAlternateK(Node head, int k) {
        if (k <= 0) throw new IllegalArgumentException("k must be positive");
        Node current = head, previousTail = null, newHead = null;
        boolean reverse = true;
        while (current != null) {
            Node groupStart = current;
            if (reverse) {
                Node previous = null;
                int count = 0;
                while (current != null && count < k) {
                    Node next = current.next;
                    current.next = previous;
                    previous = current;
                    current = next;
                    count++;
                }
                if (newHead == null) newHead = previous;
                if (previousTail != null) previousTail.next = previous;
                groupStart.next = current;
                previousTail = groupStart;
            } else {
                int count = 0;
                while (current != null && count < k) {
                    previousTail = current;
                    current = current.next;
                    count++;
                }
            }
            reverse = !reverse;
        }
        return newHead == null ? head : newHead;
    }

    public static DoublyNode reverseDoubly(DoublyNode head) {
        DoublyNode current = head, newHead = null;
        while (current != null) {
            DoublyNode next = current.next;
            current.next = current.previous;
            current.previous = next;
            newHead = current;
            current = next;
        }
        return newHead;
    }

    public static Node reverseCircular(Node head) {
        if (head == null || head.next == head) return head;
        Node previous = head, current = head.next;
        while (current != head) {
            Node next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        head.next = previous;
        return previous;
    }

    public static Node pairwiseSwap(Node head) {
        Node dummy = new Node(0);
        dummy.next = head;
        Node previous = dummy;
        while (previous.next != null && previous.next.next != null) {
            Node first = previous.next, second = first.next;
            first.next = second.next;
            second.next = first;
            previous.next = second;
            previous = first;
        }
        return dummy.next;
    }

    public static Node reorder(Node head) {
        if (head == null || head.next == null) return head;
        Node slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        Node second = reverseIterative(slow.next);
        slow.next = null;
        Node first = head;
        while (second != null) {
            Node firstNext = first.next, secondNext = second.next;
            first.next = second;
            second.next = firstNext;
            first = firstNext;
            second = secondNext;
        }
        return head;
    }

    public static Node rotateRight(Node head, int distance) {
        if (head == null || head.next == null) return head;
        int size = length(head);
        int shift = Math.floorMod(distance, size);
        if (shift == 0) return head;
        Node tail = head;
        while (tail.next != null) tail = tail.next;
        tail.next = head;
        int stepsToNewTail = size - shift - 1;
        Node newTail = head;
        for (int i = 0; i < stepsToNewTail; i++) newTail = newTail.next;
        Node newHead = newTail.next;
        newTail.next = null;
        return newHead;
    }

    public static Node middle(Node head) {
        if (head == null) return null;
        Node slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public static boolean hasCycle(Node head) {
        return cycleMeetingPoint(head) != null;
    }

    public static Node cycleStart(Node head) {
        Node meeting = cycleMeetingPoint(head);
        if (meeting == null) return null;
        Node first = head;
        while (first != meeting) {
            first = first.next;
            meeting = meeting.next;
        }
        return first;
    }

    public static int cycleLength(Node head) {
        Node meeting = cycleMeetingPoint(head);
        if (meeting == null) return 0;
        int length = 1;
        for (Node node = meeting.next; node != meeting; node = node.next) length++;
        return length;
    }

    public static void removeCycle(Node head) {
        Node start = cycleStart(head);
        if (start == null) return;
        Node tail = start;
        while (tail.next != start) tail = tail.next;
        tail.next = null;
    }

    public static boolean isPalindrome(Node head) {
        if (head == null || head.next == null) return true;
        Node slow = head, fast = head, beforeSlow = null;
        while (fast != null && fast.next != null) {
            beforeSlow = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        Node second = fast == null ? slow : slow.next;
        Node reversed = reverseIterative(second);
        boolean equal = true;
        for (Node first = head, right = reversed; right != null; first = first.next, right = right.next) {
            if (first.value != right.value) { equal = false; break; }
        }
        Node restored = reverseIterative(reversed);
        if (fast == null) beforeSlow.next = restored;
        else slow.next = restored;
        return equal;
    }

    public static Node[] splitInHalf(Node head) {
        if (head == null) return new Node[] {null, null};
        Node slow = head, fast = head, beforeSlow = null;
        while (fast != null && fast.next != null) {
            beforeSlow = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        if (beforeSlow != null) beforeSlow.next = null;
        return new Node[] {head, slow};
    }

    public static Node nthFromEnd(Node head, int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        Node fast = head, slow = head;
        for (int i = 0; i < n; i++) {
            if (fast == null) return null;
            fast = fast.next;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    public static Node intersectionNode(Node first, Node second) {
        Node a = first, b = second;
        while (a != b) {
            a = a == null ? second : a.next;
            b = b == null ? first : b.next;
        }
        return a;
    }

    public static Node mergeSorted(Node first, Node second) {
        Node dummy = new Node(0), tail = dummy;
        while (first != null && second != null) {
            if (first.value <= second.value) {
                tail.next = first;
                first = first.next;
            } else {
                tail.next = second;
                second = second.next;
            }
            tail = tail.next;
        }
        tail.next = first == null ? second : first;
        return dummy.next;
    }

    public static Node mergeSortedRecursive(Node first, Node second) {
        if (first == null) return second;
        if (second == null) return first;
        if (first.value <= second.value) {
            first.next = mergeSortedRecursive(first.next, second);
            return first;
        }
        second.next = mergeSortedRecursive(first, second.next);
        return second;
    }

    public static Node mergeKSorted(Node[] lists) { return mergeKSorted(lists, 0, lists.length); }

    public static Node sortMerge(Node head) {
        if (head == null || head.next == null) return head;
        Node slow = head, fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        Node second = slow.next;
        slow.next = null;
        return mergeSorted(sortMerge(head), sortMerge(second));
    }

    public static Node insertionSort(Node head) {
        Node sorted = null;
        while (head != null) {
            Node next = head.next;
            if (sorted == null || head.value < sorted.value) {
                head.next = sorted;
                sorted = head;
            } else {
                Node position = sorted;
                while (position.next != null && position.next.value <= head.value) position = position.next;
                head.next = position.next;
                position.next = head;
            }
            head = next;
        }
        return sorted;
    }

    public static Node removeDuplicatesSorted(Node head) {
        for (Node node = head; node != null; node = node.next) {
            while (node.next != null && node.next.value == node.value) node.next = node.next.next;
        }
        return head;
    }

    public static Node removeDuplicatesUnsorted(Node head) {
        for (Node current = head; current != null; current = current.next) {
            Node previous = current;
            while (previous.next != null) {
                if (previous.next.value == current.value) previous.next = previous.next.next;
                else previous = previous.next;
            }
        }
        return head;
    }

    public static Node mergeAlternate(Node first, Node second) {
        Node firstHead = first;
        while (first != null && second != null) {
            Node firstNext = first.next, secondNext = second.next;
            first.next = second;
            second.next = firstNext;
            first = firstNext;
            second = secondNext;
        }
        return firstHead;
    }

    public static Node unionSorted(Node first, Node second) {
        Node merged = mergeSorted(copy(first), copy(second));
        return removeDuplicatesSorted(merged);
    }

    public static Node intersectionSorted(Node first, Node second) {
        Node dummy = new Node(0), tail = dummy;
        while (first != null && second != null) {
            if (first.value == second.value) {
                if (tail == dummy || tail.value != first.value) {
                    tail.next = new Node(first.value);
                    tail = tail.next;
                }
                first = first.next;
                second = second.next;
            } else if (first.value < second.value) first = first.next;
            else second = second.next;
        }
        return dummy.next;
    }

    public static Node addReversedNumbers(Node first, Node second) {
        Node dummy = new Node(0), tail = dummy;
        int carry = 0;
        while (first != null || second != null || carry != 0) {
            int sum = carry + (first == null ? 0 : first.value) + (second == null ? 0 : second.value);
            tail.next = new Node(sum % 10);
            tail = tail.next;
            carry = sum / 10;
            if (first != null) first = first.next;
            if (second != null) second = second.next;
        }
        return dummy.next;
    }

    public static Node multiplyReversedNumbers(Node first, Node second) {
        int[] a = digits(first), b = digits(second);
        if (a.length == 0 || b.length == 0) return new Node(0);
        long[] product = new long[a.length + b.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) product[i + j] += (long) a[i] * b[j];
        }
        for (int i = 0; i < product.length - 1; i++) {
            product[i + 1] += product[i] / 10;
            product[i] %= 10;
        }
        int length = product.length;
        while (length > 1 && product[length - 1] == 0) length--;
        Node dummy = new Node(0), tail = dummy;
        for (int i = 0; i < length; i++) {
            tail.next = new Node((int) product[i]);
            tail = tail.next;
        }
        return dummy.next;
    }

    public static Node oddEvenPositions(Node head) {
        if (head == null) return null;
        Node odd = head, even = head.next, evenHead = even;
        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;
            even.next = odd.next;
            even = even.next;
        }
        odd.next = evenHead;
        return head;
    }

    public static Node partition(Node head, int pivot) {
        Node lessDummy = new Node(0), greaterDummy = new Node(0), less = lessDummy, greater = greaterDummy;
        while (head != null) {
            Node next = head.next;
            head.next = null;
            if (head.value < pivot) { less.next = head; less = head; }
            else { greater.next = head; greater = head; }
            head = next;
        }
        less.next = greaterDummy.next;
        return lessDummy.next;
    }

    public static Node segregateEvenOddValues(Node head) {
        Node evenDummy = new Node(0), oddDummy = new Node(0), even = evenDummy, odd = oddDummy;
        while (head != null) {
            Node next = head.next;
            head.next = null;
            if ((head.value & 1) == 0) { even.next = head; even = head; }
            else { odd.next = head; odd = head; }
            head = next;
        }
        even.next = oddDummy.next;
        return evenDummy.next;
    }

    public static Node moveLastToFront(Node head) {
        if (head == null || head.next == null) return head;
        Node previous = null, last = head;
        while (last.next != null) { previous = last; last = last.next; }
        previous.next = null;
        last.next = head;
        return last;
    }

    public static Node deleteNodesWithGreaterValueOnRight(Node head) {
        Node reversed = reverseIterative(head), kept = null;
        int maximum = Integer.MIN_VALUE;
        while (reversed != null) {
            Node next = reversed.next;
            if (reversed.value >= maximum) {
                maximum = reversed.value;
                reversed.next = kept;
                kept = reversed;
            }
            reversed = next;
        }
        return kept;
    }

    public static RandomNode cloneRandomList(RandomNode head) {
        RandomNode cloneHead = null, cloneTail = null;
        for (RandomNode current = head; current != null; current = current.next) {
            RandomNode clone = new RandomNode(current.value);
            if (cloneHead == null) cloneHead = clone;
            else cloneTail.next = clone;
            cloneTail = clone;
        }
        for (RandomNode original = head, clone = cloneHead; original != null; original = original.next, clone = clone.next) {
            if (original.random != null) {
                RandomNode source = head, copyNode = cloneHead;
                while (source != null && source != original.random) {
                    source = source.next;
                    copyNode = copyNode.next;
                }
                if (source == null) throw new IllegalArgumentException("Random pointer must target this list");
                clone.random = copyNode;
            }
        }
        return cloneHead;
    }

    public static MultiLevelNode flattenSortedDownLists(MultiLevelNode head) {
        if (head == null || head.next == null) return head;
        head.next = flattenSortedDownLists(head.next);
        return mergeDown(head, head.next);
    }

    public static MultiLevelNode flattenMultilevel(MultiLevelNode head) {
        if (head == null) return null;
        flattenMultilevel(head.next);
        if (head.child != null) {
            MultiLevelNode childHead = flattenMultilevel(head.child);
            MultiLevelNode tail = childHead;
            while (tail.next != null) tail = tail.next;
            tail.next = head.next;
            head.next = childHead;
            head.child = null;
        }
        return head;
    }

    public static Node swapNodes(Node head, int firstValue, int secondValue) {
        if (firstValue == secondValue) return head;
        Node dummy = new Node(0);
        dummy.next = head;
        Node previousFirst = dummy, previousSecond = dummy;
        while (previousFirst.next != null && previousFirst.next.value != firstValue) previousFirst = previousFirst.next;
        while (previousSecond.next != null && previousSecond.next.value != secondValue) previousSecond = previousSecond.next;
        if (previousFirst.next == null || previousSecond.next == null) return head;
        Node first = previousFirst.next, second = previousSecond.next;
        if (first.next == second) {
            previousFirst.next = second;
            first.next = second.next;
            second.next = first;
        } else if (second.next == first) {
            previousSecond.next = first;
            second.next = first.next;
            first.next = second;
        } else {
            Node temporary = first.next;
            previousFirst.next = second;
            previousSecond.next = first;
            first.next = second.next;
            second.next = temporary;
        }
        return dummy.next;
    }

    public static DoublyNode insertDoubly(DoublyNode head, int index, int value) {
        if (index < 0) throw new IndexOutOfBoundsException("Index: " + index);
        DoublyNode node = new DoublyNode(value);
        if (index == 0) {
            node.next = head;
            if (head != null) head.previous = node;
            return node;
        }
        DoublyNode previous = head;
        for (int i = 0; i < index - 1 && previous != null; i++) previous = previous.next;
        if (previous == null) throw new IndexOutOfBoundsException("Index: " + index);
        node.next = previous.next;
        node.previous = previous;
        previous.next = node;
        if (node.next != null) node.next.previous = node;
        return head;
    }

    public static DoublyNode deleteDoubly(DoublyNode head, int index) {
        if (index < 0) throw new IndexOutOfBoundsException("Index: " + index);
        DoublyNode node = head;
        for (int i = 0; i < index && node != null; i++) node = node.next;
        if (node == null) throw new IndexOutOfBoundsException("Index: " + index);
        if (node.previous != null) node.previous.next = node.next;
        else head = node.next;
        if (node.next != null) node.next.previous = node.previous;
        return head;
    }

    public static DoublyNode insertCircularDoubly(DoublyNode head, int index, int value) {
        int count = circularDoublyLength(head);
        if (index < 0 || index > count) throw new IndexOutOfBoundsException("Index: " + index);
        DoublyNode node = new DoublyNode(value);
        if (head == null) {
            node.next = node;
            node.previous = node;
            return node;
        }
        DoublyNode next = index == count ? head : head;
        for (int i = 0; i < index; i++) next = next.next;
        DoublyNode previous = next.previous;
        node.previous = previous;
        node.next = next;
        previous.next = node;
        next.previous = node;
        return index == 0 ? node : head;
    }

    public static DoublyNode deleteCircularDoubly(DoublyNode head, int index) {
        int count = circularDoublyLength(head);
        if (index < 0 || index >= count) throw new IndexOutOfBoundsException("Index: " + index);
        DoublyNode removed = head;
        for (int i = 0; i < index; i++) removed = removed.next;
        if (count == 1) return null;
        removed.previous.next = removed.next;
        removed.next.previous = removed.previous;
        return removed == head ? removed.next : head;
    }

    public static int[] traverseCircularDoubly(DoublyNode head) {
        int count = circularDoublyLength(head);
        int[] values = new int[count];
        DoublyNode node = head;
        for (int i = 0; i < count; i++, node = node.next) values[i] = node.value;
        return values;
    }

    public static TreeNode sortedDoublyToBalancedBst(DoublyNode head) {
        int count = 0;
        for (DoublyNode node = head; node != null; node = node.next) count++;
        DoublyNode[] cursor = new DoublyNode[] {head};
        return buildBalancedBst(cursor, count);
    }

    public static int[] traverseDoubly(DoublyNode head) {
        int count = 0;
        for (DoublyNode node = head; node != null; node = node.next) count++;
        int[] values = new int[count];
        int index = 0;
        for (DoublyNode node = head; node != null; node = node.next) values[index++] = node.value;
        return values;
    }

    public static int[] traverseCircular(Node head) {
        if (head == null) return new int[0];
        int count = 1;
        for (Node node = head.next; node != head; node = node.next) {
            if (node == null) throw new IllegalArgumentException("List is not circular");
            count++;
        }
        int[] values = new int[count];
        Node node = head;
        for (int i = 0; i < count; i++, node = node.next) values[i] = node.value;
        return values;
    }

    public static Node insertCircular(Node head, int index, int value) {
        int count = head == null ? 0 : traverseCircular(head).length;
        if (index < 0 || index > count) throw new IndexOutOfBoundsException("Index: " + index);
        Node node = new Node(value);
        if (head == null) { node.next = node; return node; }
        if (index == 0) {
            Node tail = head;
            while (tail.next != head) tail = tail.next;
            node.next = head;
            tail.next = node;
            return node;
        }
        Node previous = head;
        for (int i = 1; i < index; i++) previous = previous.next;
        node.next = previous.next;
        previous.next = node;
        return head;
    }

    public static Node deleteCircular(Node head, int index) {
        if (head == null) throw new IndexOutOfBoundsException("Empty circular list");
        int count = traverseCircular(head).length;
        if (index < 0 || index >= count) throw new IndexOutOfBoundsException("Index: " + index);
        if (count == 1) return null;
        if (index == 0) {
            Node tail = head;
            while (tail.next != head) tail = tail.next;
            head = head.next;
            tail.next = head;
            return head;
        }
        Node previous = head;
        for (int i = 1; i < index; i++) previous = previous.next;
        previous.next = previous.next.next;
        return head;
    }

    public static Node[] splitCircular(Node head) {
        if (head == null) return new Node[] {null, null};
        if (head.next == head) return new Node[] {head, null};
        Node slow = head, fast = head;
        while (fast.next != head && fast.next.next != head) {
            slow = slow.next;
            fast = fast.next.next;
        }
        if (fast.next.next == head) fast = fast.next;
        Node second = slow.next;
        slow.next = head;
        fast.next = second;
        return new Node[] {head, second};
    }

    public static Node makeCircular(Node head) {
        if (head == null) return null;
        Node tail = head;
        while (tail.next != null) tail = tail.next;
        tail.next = head;
        return head;
    }

    public static int josephus(int people, int step) {
        if (people <= 0 || step <= 0) throw new IllegalArgumentException("People and step must be positive");
        int survivor = 0;
        for (int size = 1; size <= people; size++) survivor = (survivor + step) % size;
        return survivor + 1;
    }

    public static boolean isHappyNumber(int number) {
        if (number <= 0) return false;
        int slow = number, fast = number;
        do {
            slow = sumOfSquaredDigits(slow);
            fast = sumOfSquaredDigits(sumOfSquaredDigits(fast));
        } while (slow != fast);
        return slow == 1;
    }

    public static Node recursiveInsertSorted(Node head, int value) {
        if (head == null || value <= head.value) {
            Node node = new Node(value);
            node.next = head;
            return node;
        }
        head.next = recursiveInsertSorted(head.next, value);
        return head;
    }

    public static int recursiveLength(Node head) { return head == null ? 0 : 1 + recursiveLength(head.next); }
    public static int recursiveSearch(Node head, int target) {
        if (head == null) return -1;
        if (head.value == target) return 0;
        int result = recursiveSearch(head.next, target);
        return result < 0 ? -1 : result + 1;
    }
    public static Node recursiveDelete(Node head, int value) {
        if (head == null) return null;
        if (head.value == value) return head.next;
        head.next = recursiveDelete(head.next, value);
        return head;
    }
    public static int[] recursiveTraversal(Node head) {
        int[] result = new int[recursiveLength(head)];
        fillRecursive(head, result, 0);
        return result;
    }
    public static boolean recursivePalindrome(Node head) {
        Node[] cursor = new Node[] {head};
        return recursivePalindrome(head, cursor);
    }

    private static boolean recursivePalindrome(Node right, Node[] left) {
        if (right == null) return true;
        if (!recursivePalindrome(right.next, left)) return false;
        boolean matches = left[0].value == right.value;
        left[0] = left[0].next;
        return matches;
    }

    private static void fillRecursive(Node node, int[] output, int index) {
        if (node == null) return;
        output[index] = node.value;
        fillRecursive(node.next, output, index + 1);
    }

    private static Node mergeKSorted(Node[] lists, int left, int right) {
        if (left >= right) return null;
        if (right - left == 1) return lists[left];
        int middle = left + (right - left) / 2;
        return mergeSorted(mergeKSorted(lists, left, middle), mergeKSorted(lists, middle, right));
    }

    private static Node cycleMeetingPoint(Node head) {
        Node slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return slow;
        }
        return null;
    }

    private static Node copy(Node head) {
        Node dummy = new Node(0), tail = dummy;
        while (head != null) {
            tail.next = new Node(head.value);
            tail = tail.next;
            head = head.next;
        }
        return dummy.next;
    }

    private static int[] digits(Node head) {
        int count = length(head), index = 0;
        int[] result = new int[count];
        for (Node node = head; node != null; node = node.next) {
            if (node.value < 0 || node.value > 9) throw new IllegalArgumentException("Digits must be from 0 to 9");
            result[index++] = node.value;
        }
        return result;
    }

    private static int sumOfSquaredDigits(int number) {
        int sum = 0;
        while (number > 0) {
            int digit = number % 10;
            sum += digit * digit;
            number /= 10;
        }
        return sum;
    }

    private static MultiLevelNode mergeDown(MultiLevelNode first, MultiLevelNode second) {
        MultiLevelNode dummy = new MultiLevelNode(0), tail = dummy;
        while (first != null && second != null) {
            if (first.value <= second.value) {
                tail.down = first;
                first = first.down;
            } else {
                tail.down = second;
                second = second.down;
            }
            tail = tail.down;
            tail.next = null;
        }
        tail.down = first == null ? second : first;
        return dummy.down;
    }

    private static int circularDoublyLength(DoublyNode head) {
        if (head == null) return 0;
        int count = 1;
        DoublyNode node = head.next;
        while (node != head) {
            if (node == null) throw new IllegalArgumentException("List is not circular");
            count++;
            node = node.next;
        }
        return count;
    }

    private static TreeNode buildBalancedBst(DoublyNode[] cursor, int count) {
        if (count == 0) return null;
        TreeNode left = buildBalancedBst(cursor, count / 2);
        TreeNode root = new TreeNode(cursor[0].value);
        root.left = left;
        cursor[0] = cursor[0].next;
        root.right = buildBalancedBst(cursor, count - count / 2 - 1);
        return root;
    }
}
