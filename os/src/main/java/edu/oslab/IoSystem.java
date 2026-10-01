package edu.oslab;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

public final class IoSystem {
    public record IoRequest(int requestId, String process, String operation, String device) { }

    private final Map<String, Queue<IoRequest>> deviceQueues = new LinkedHashMap<>();
    private int nextRequestId = 1;

    public IoRequest submit(String process, String operation, String device) {
        if (process == null || process.isBlank() || operation == null || operation.isBlank()
                || device == null || device.isBlank()) {
            throw new IllegalArgumentException("Process, operation, and device are required");
        }
        IoRequest request = new IoRequest(nextRequestId++, process, operation, device);
        deviceQueues.computeIfAbsent(device, ignored -> new ArrayDeque<>()).add(request);
        return request;
    }

    public IoRequest completeNext(String device) {
        if (device == null || device.isBlank()) {
            throw new IllegalArgumentException("Device name must not be blank");
        }
        Queue<IoRequest> queue = deviceQueues.get(device);
        IoRequest request = queue == null ? null : queue.poll();
        if (request == null) {
            throw new IllegalStateException("There are no pending I/O requests for " + device);
        }
        return request;
    }

    public int pendingCount() {
        return deviceQueues.values().stream().mapToInt(Queue::size).sum();
    }

    public static void runDemo() {
        IoSystem io = new IoSystem();
        io.submit("P1", "read block 4", "disk-0");
        io.submit("P2", "print page", "printer-0");
        io.submit("P3", "read block 9", "disk-0");
        System.out.println("Device completed: " + io.completeNext("disk-0"));
        System.out.println("Device completed: " + io.completeNext("printer-0"));
        System.out.println("Device completed: " + io.completeNext("disk-0"));
    }
}
