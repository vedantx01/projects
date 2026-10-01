package dsa.linked_list;

import dsa.array.HashTableInt;

public final class LinkedListStructures {
    private LinkedListStructures() { }

    public static final class Stack {
        private LinkedListAlgorithms.Node top;
        private int size;
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
        public void push(int value) { top = LinkedListAlgorithms.insertBeginning(top, value); size++; }
        public int peek() {
            if (top == null) throw new IllegalStateException("Stack is empty");
            return top.value;
        }
        public int pop() {
            int value = peek();
            top = top.next;
            size--;
            return value;
        }
    }

    public static final class Queue {
        private LinkedListAlgorithms.Node head;
        private LinkedListAlgorithms.Node tail;
        private int size;
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
        public void offer(int value) {
            LinkedListAlgorithms.Node node = new LinkedListAlgorithms.Node(value);
            if (tail == null) head = node;
            else tail.next = node;
            tail = node;
            size++;
        }
        public int peek() {
            if (head == null) throw new IllegalStateException("Queue is empty");
            return head.value;
        }
        public int poll() {
            int value = peek();
            head = head.next;
            if (head == null) tail = null;
            size--;
            return value;
        }
    }

    public static final class CircularQueue {
            private LinkedListAlgorithms.Node head;
            private LinkedListAlgorithms.Node tail;
            private int size;
            public int size() { return size; }
            public boolean isEmpty() { return size == 0; }
            public void offer(int value) {
                LinkedListAlgorithms.Node node = new LinkedListAlgorithms.Node(value);
                if (tail == null) {
                    head = tail = node;
                    node.next = node;
                } else {
                    node.next = head;
                    tail.next = node;
                    tail = node;
                }
                size++;
            }
            public int peek() {
                if (head == null) throw new IllegalStateException("Queue is empty");
                return head.value;
            }
            public int poll() {
                int value = peek();
                if (size == 1) head = tail = null;
                else {
                    head = head.next;
                    tail.next = head;
                }
                size--;
                return value;
            }
        }

    public static final class Deque {
        private LinkedListAlgorithms.DoublyNode head;
        private LinkedListAlgorithms.DoublyNode tail;
        private int size;
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
        public void addFirst(int value) {
            LinkedListAlgorithms.DoublyNode node = new LinkedListAlgorithms.DoublyNode(value);
            node.next = head;
            if (head == null) tail = node;
            else head.previous = node;
            head = node;
            size++;
        }
        public void addLast(int value) {
            LinkedListAlgorithms.DoublyNode node = new LinkedListAlgorithms.DoublyNode(value);
            node.previous = tail;
            if (tail == null) head = node;
            else tail.next = node;
            tail = node;
            size++;
        }
        public int removeFirst() {
            if (head == null) throw new IllegalStateException("Deque is empty");
            int value = head.value;
            head = head.next;
            if (head == null) tail = null;
            else head.previous = null;
            size--;
            return value;
        }
        public int removeLast() {
            if (tail == null) throw new IllegalStateException("Deque is empty");
            int value = tail.value;
            tail = tail.previous;
            if (tail == null) head = null;
            else tail.next = null;
            size--;
            return value;
        }
    }

    public static final class LruCache {
        private final int capacity;
        private final HashTableInt slots;
        private final int[] keys;
        private final int[] values;
        private final int[] previous;
        private final int[] next;
        private final boolean[] active;
        private int head = -1;
        private int tail = -1;
        private int size;

        public LruCache(int capacity) {
            if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
            this.capacity = capacity;
            slots = new HashTableInt(Math.max(2, capacity * 2));
            keys = new int[capacity];
            values = new int[capacity];
            previous = new int[capacity];
            next = new int[capacity];
            active = new boolean[capacity];
        }

        public int size() { return size; }
        public int get(int key) {
            if (!slots.containsKey(key)) return -1;
            int slot = slots.get(key);
            moveToFront(slot);
            return values[slot];
        }

        public void put(int key, int value) {
            if (capacity == 0) return;
            if (slots.containsKey(key)) {
                int slot = slots.get(key);
                values[slot] = value;
                moveToFront(slot);
                return;
            }
            int slot;
            if (size == capacity) {
                slot = tail;
                slots.remove(keys[slot]);
                unlink(slot);
            } else {
                slot = freeSlot();
                size++;
            }
            keys[slot] = key;
            values[slot] = value;
            active[slot] = true;
            slots.put(key, slot);
            linkFront(slot);
        }

        private int freeSlot() {
            for (int i = 0; i < active.length; i++) if (!active[i]) return i;
            throw new IllegalStateException("No free cache slot");
        }
        private void moveToFront(int slot) {
            if (slot == head) return;
            unlink(slot);
            linkFront(slot);
        }
        private void unlink(int slot) {
            if (previous[slot] >= 0) next[previous[slot]] = next[slot];
            else head = next[slot];
            if (next[slot] >= 0) previous[next[slot]] = previous[slot];
            else tail = previous[slot];
            previous[slot] = next[slot] = -1;
        }
        private void linkFront(int slot) {
            previous[slot] = -1;
            next[slot] = head;
            if (head >= 0) previous[head] = slot;
            else tail = slot;
            head = slot;
        }
    }

    public static final class LfuCache {
        private final int[] keys;
        private final int[] values;
        private final int[] frequencies;
        private final long[] timestamps;
        private final boolean[] active;
        private int size;
        private long clock;

        public LfuCache(int capacity) {
            if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
            keys = new int[capacity];
            values = new int[capacity];
            frequencies = new int[capacity];
            timestamps = new long[capacity];
            active = new boolean[capacity];
        }

        public int get(int key) {
            int slot = find(key);
            if (slot < 0) return -1;
            frequencies[slot]++;
            timestamps[slot] = ++clock;
            return values[slot];
        }

        public void put(int key, int value) {
            if (active.length == 0) return;
            int slot = find(key);
            if (slot >= 0) {
                values[slot] = value;
                frequencies[slot]++;
                timestamps[slot] = ++clock;
                return;
            }
            if (size < active.length) {
                slot = 0;
                while (active[slot]) slot++;
                size++;
            } else {
                slot = 0;
                for (int i = 1; i < active.length; i++) {
                    if (frequencies[i] < frequencies[slot]
                        || (frequencies[i] == frequencies[slot] && timestamps[i] < timestamps[slot])) slot = i;
                }
            }
            keys[slot] = key;
            values[slot] = value;
            frequencies[slot] = 1;
            timestamps[slot] = ++clock;
            active[slot] = true;
        }

        private int find(int key) {
            for (int i = 0; i < active.length; i++) if (active[i] && keys[i] == key) return i;
            return -1;
        }
    }

    public static final class BrowserHistory {
        private static final class Page {
            final String address;
            Page previous;
            Page next;
            Page(String address) { this.address = address; }
        }
        private Page current;
        public BrowserHistory(String homepage) {
            if (homepage == null) throw new IllegalArgumentException("Homepage cannot be null");
            current = new Page(homepage);
        }
        public String current() { return current.address; }
        public String visit(String address) {
            if (address == null) throw new IllegalArgumentException("Address cannot be null");
            current.next = null;
            Page page = new Page(address);
            page.previous = current;
            current.next = page;
            current = page;
            return current.address;
        }
        public String back(int steps) {
            if (steps < 0) throw new IllegalArgumentException("Steps cannot be negative");
            while (steps-- > 0 && current.previous != null) current = current.previous;
            return current.address;
        }
        public String forward(int steps) {
            if (steps < 0) throw new IllegalArgumentException("Steps cannot be negative");
            while (steps-- > 0 && current.next != null) current = current.next;
            return current.address;
        }
    }

    public static final class MusicPlaylist {
        private static final class Song {
            final String name;
            Song previous;
            Song next;
            Song(String name) { this.name = name; }
        }
        private Song current;
        private int size;
        public int size() { return size; }
        public boolean isEmpty() { return current == null; }
        public void add(String name) {
            if (name == null) throw new IllegalArgumentException("Song name cannot be null");
            Song song = new Song(name);
            if (current == null) {
                song.next = song.previous = song;
                current = song;
            } else {
                Song tail = current.previous;
                song.previous = tail;
                song.next = current;
                tail.next = song;
                current.previous = song;
            }
            size++;
        }
        public String current() {
            if (current == null) throw new IllegalStateException("Playlist is empty");
            return current.name;
        }
        public String next() {
            if (current == null) throw new IllegalStateException("Playlist is empty");
            current = current.next;
            return current.name;
        }
        public String previous() {
            if (current == null) throw new IllegalStateException("Playlist is empty");
            current = current.previous;
            return current.name;
        }
        public String removeCurrent() {
            if (current == null) throw new IllegalStateException("Playlist is empty");
            String removed = current.name;
            if (size == 1) current = null;
            else {
                current.previous.next = current.next;
                current.next.previous = current.previous;
                current = current.next;
            }
            size--;
            return removed;
        }
    }

    public static final class TextEditor {
        private static final class CharacterNode {
            char value;
            CharacterNode previous;
            CharacterNode next;
            CharacterNode(char value) { this.value = value; }
        }

        private final CharacterNode sentinel = new CharacterNode('\0');
        private CharacterNode cursorPrevious = sentinel;
        private int length;

        public int length() { return length; }

        public void insert(String text) {
            if (text == null) throw new IllegalArgumentException("Text cannot be null");
            for (int i = 0; i < text.length(); i++) {
                CharacterNode node = new CharacterNode(text.charAt(i));
                CharacterNode following = cursorPrevious.next;
                node.previous = cursorPrevious;
                node.next = following;
                cursorPrevious.next = node;
                if (following != null) following.previous = node;
                cursorPrevious = node;
                length++;
            }
        }

        public boolean backspace() {
            if (cursorPrevious == sentinel) return false;
            CharacterNode removed = cursorPrevious;
            cursorPrevious = removed.previous;
            cursorPrevious.next = removed.next;
            if (removed.next != null) removed.next.previous = cursorPrevious;
            length--;
            return true;
        }

        public boolean delete() {
            CharacterNode removed = cursorPrevious.next;
            if (removed == null) return false;
            cursorPrevious.next = removed.next;
            if (removed.next != null) removed.next.previous = cursorPrevious;
            length--;
            return true;
        }

        public void moveLeft(int characters) {
            if (characters < 0) throw new IllegalArgumentException("Character count cannot be negative");
            while (characters-- > 0 && cursorPrevious != sentinel) cursorPrevious = cursorPrevious.previous;
        }

        public void moveRight(int characters) {
            if (characters < 0) throw new IllegalArgumentException("Character count cannot be negative");
            while (characters-- > 0 && cursorPrevious.next != null) cursorPrevious = cursorPrevious.next;
        }

        @Override
        public String toString() {
            char[] text = new char[length];
            int index = 0;
            for (CharacterNode node = sentinel.next; node != null; node = node.next) text[index++] = node.value;
            return new String(text);
        }
    }

    public static final class UndoRedoText {
        private static final class Action {
            final char value;
            final boolean insertion;
            Action previous;
            Action next;
            Action(char value, boolean insertion) { this.value = value; this.insertion = insertion; }
        }
        private final TextEditor editor = new TextEditor();
        private final Action sentinel = new Action('\0', false);
        private Action current = sentinel;

        public void insert(String text) {
            if (text == null) throw new IllegalArgumentException("Text cannot be null");
            for (int i = 0; i < text.length(); i++) {
                discardRedo();
                char value = text.charAt(i);
                editor.moveRight(Integer.MAX_VALUE);
                editor.insert(String.valueOf(value));
                appendAction(new Action(value, true));
            }
        }

        public boolean deleteLast() {
            editor.moveRight(Integer.MAX_VALUE);
            String text = editor.toString();
            if (text.length() == 0) return false;
            discardRedo();
            char removed = text.charAt(text.length() - 1);
            editor.backspace();
            appendAction(new Action(removed, false));
            return true;
        }

        public boolean undo() {
            if (current == sentinel) return false;
            if (current.insertion) {
                editor.moveRight(Integer.MAX_VALUE);
                editor.backspace();
            } else {
                editor.moveRight(Integer.MAX_VALUE);
                editor.insert(String.valueOf(current.value));
            }
            current = current.previous;
            return true;
        }

        public boolean redo() {
            Action next = current.next;
            if (next == null) return false;
            editor.moveRight(Integer.MAX_VALUE);
            if (next.insertion) editor.insert(String.valueOf(next.value));
            else editor.backspace();
            current = next;
            return true;
        }

        public String text() { return editor.toString(); }

        private void appendAction(Action action) {
            action.previous = current;
            current.next = action;
            current = action;
        }

        private void discardRedo() { current.next = null; }
    }

    public static final class PriorityQueue {
        private static final class Entry {
            final int value;
            final int priority;
            Entry next;
            Entry(int value, int priority) { this.value = value; this.priority = priority; }
        }
        private Entry head;
        private int size;
        public int size() { return size; }
        public boolean isEmpty() { return head == null; }
        public void offer(int value, int priority) {
            Entry entry = new Entry(value, priority);
            if (head == null || priority < head.priority) {
                entry.next = head;
                head = entry;
            } else {
                Entry current = head;
                while (current.next != null && current.next.priority <= priority) current = current.next;
                entry.next = current.next;
                current.next = entry;
            }
            size++;
        }
        public int poll() {
            if (head == null) throw new IllegalStateException("Priority queue is empty");
            int value = head.value;
            head = head.next;
            size--;
            return value;
        }
    }
}
