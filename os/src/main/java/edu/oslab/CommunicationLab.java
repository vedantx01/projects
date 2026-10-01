package edu.oslab;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CommunicationLab {
    public enum EndpointType {
        PROCESS,
        THREAD
    }

    public record Endpoint(EndpointType type, int processId, int threadId) {
        public Endpoint {
            if (type == null || processId < 1 || (type == EndpointType.THREAD && threadId < 1)) {
                throw new IllegalArgumentException("Invalid communication endpoint");
            }
        }
    }

    public record Message(Endpoint from, Endpoint to, String payload) { }

    private final Map<Endpoint, Queue<Message>> mailboxes = new LinkedHashMap<>();

    public void send(Endpoint from, Endpoint to, String payload) {
        if (from == null || to == null || payload == null || payload.isBlank()) {
            throw new IllegalArgumentException("Sender, receiver, and a non-empty payload are required");
        }
        mailboxes.computeIfAbsent(to, ignored -> new ArrayDeque<>())
                .add(new Message(from, to, payload));
    }

    public Message receive(Endpoint endpoint) {
        if (endpoint == null) {
            throw new IllegalArgumentException("Receiver endpoint is required");
        }
        Queue<Message> mailbox = mailboxes.get(endpoint);
        Message message = mailbox == null ? null : mailbox.poll();
        if (message == null) {
            throw new IllegalStateException("No message is waiting for " + endpoint);
        }
        return message;
    }

    public static void runDemo() {
        CommunicationLab bus = new CommunicationLab();
        Endpoint processA = new Endpoint(EndpointType.PROCESS, 10, 0);
        Endpoint processB = new Endpoint(EndpointType.PROCESS, 20, 0);
        Endpoint threadA = new Endpoint(EndpointType.THREAD, 10, 1);
        Endpoint threadB = new Endpoint(EndpointType.THREAD, 20, 2);
        bus.send(processA, processB, "process-to-process");
        bus.send(processA, threadB, "process-to-thread");
        bus.send(threadA, processB, "thread-to-process");
        bus.send(threadA, threadB, "thread-to-thread");
        System.out.println("P2P mailbox: " + bus.receive(processB));
        System.out.println("T2P mailbox: " + bus.receive(processB));
        System.out.println("P2T mailbox: " + bus.receive(threadB));
        System.out.println("T2T mailbox: " + bus.receive(threadB));
    }
}
