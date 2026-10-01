package edu.oslab;

import java.util.ArrayList;
import java.util.List;

public final class MemoryManager {
    public record Block(int start, int size, boolean free, String owner) { }

    private static final class Segment {
        private int start;
        private int size;
        private String owner;

        private Segment(int start, int size, String owner) {
            this.start = start;
            this.size = size;
            this.owner = owner;
        }
    }

    private final int capacity;
    private final List<Segment> segments = new ArrayList<>();

    public MemoryManager(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Memory capacity must be positive");
        }
        this.capacity = capacity;
        segments.add(new Segment(0, capacity, null));
    }

    public int allocate(String owner, int size) {
        if (owner == null || owner.isBlank() || size < 1 || size > capacity) {
            throw new IllegalArgumentException("Provide a valid owner and allocation size");
        }
        if (segments.stream().anyMatch(segment -> owner.equals(segment.owner))) {
            throw new IllegalArgumentException("Owner already has an allocation: " + owner);
        }
        for (int index = 0; index < segments.size(); index++) {
            Segment free = segments.get(index);
            if (free.owner == null && free.size >= size) {
                int address = free.start;
                if (free.size == size) {
                    free.owner = owner;
                } else {
                    segments.add(index + 1, new Segment(free.start + size, free.size - size, null));
                    free.size = size;
                    free.owner = owner;
                }
                return address;
            }
        }
        throw new IllegalStateException("Not enough contiguous free memory for " + owner);
    }

    public void release(String owner) {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Owner must not be blank");
        }
        Segment allocated = segments.stream()
                .filter(segment -> owner.equals(segment.owner))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No allocation for owner: " + owner));
        allocated.owner = null;
        coalesce();
    }

    public List<Block> blocks() {
        return segments.stream()
                .map(segment -> new Block(segment.start, segment.size,
                        segment.owner == null, segment.owner))
                .toList();
    }

    public int freeBytes() {
        return segments.stream().filter(segment -> segment.owner == null)
                .mapToInt(segment -> segment.size).sum();
    }

    private void coalesce() {
        for (int index = 0; index < segments.size() - 1; ) {
            Segment current = segments.get(index);
            Segment next = segments.get(index + 1);
            if (current.owner == null && next.owner == null) {
                current.size += next.size;
                segments.remove(index + 1);
            } else {
                index++;
            }
        }
    }

    public static void runDemo() {
        MemoryManager memory = new MemoryManager(1024);
        memory.allocate("P1", 256);
        memory.allocate("P2", 384);
        memory.allocate("P3", 128);
        memory.release("P2");
        memory.allocate("P4", 192);
        memory.blocks().forEach(System.out::println);
        System.out.println("Free bytes: " + memory.freeBytes());
    }
}
