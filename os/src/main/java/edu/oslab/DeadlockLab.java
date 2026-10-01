package edu.oslab;

import java.util.ArrayList;
import java.util.List;

public final class DeadlockLab {
    public record ResourceRequest(int processId, int resourceId) { }

    private DeadlockLab() { }

    public static List<Integer> detectCycle(int processCount, List<ResourceRequest> waitsFor) {
        if (processCount < 1) {
            throw new IllegalArgumentException("Process count must be positive");
        }
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < processCount; i++) {
            graph.add(new ArrayList<>());
        }
        for (ResourceRequest edge : waitsFor) {
            if (edge.processId() < 0 || edge.resourceId() < 0
                    || edge.processId() >= processCount || edge.resourceId() >= processCount) {
                throw new IllegalArgumentException("Wait-for graph node is out of range");
            }
            graph.get(edge.processId()).add(edge.resourceId());
        }
        int[] state = new int[processCount];
        List<Integer> stack = new ArrayList<>();
        for (int node = 0; node < processCount; node++) {
            List<Integer> cycle = visit(node, graph, state, stack);
            if (!cycle.isEmpty()) {
                return cycle;
            }
        }
        return List.of();
    }

    private static List<Integer> visit(int node, List<List<Integer>> graph,
                                       int[] state, List<Integer> stack) {
        if (state[node] == 2) {
            return List.of();
        }
        if (state[node] == 1) {
            int cycleStart = stack.indexOf(node);
            List<Integer> cycle = new ArrayList<>(stack.subList(cycleStart, stack.size()));
            cycle.add(node);
            return cycle;
        }
        state[node] = 1;
        stack.add(node);
        for (int neighbor : graph.get(node)) {
            List<Integer> cycle = visit(neighbor, graph, state, stack);
            if (!cycle.isEmpty()) {
                return cycle;
            }
        }
        stack.remove(stack.size() - 1);
        state[node] = 2;
        return List.of();
    }

    public static void runDemo() {
        List<ResourceRequest> cycle = List.of(
                new ResourceRequest(0, 1),
                new ResourceRequest(1, 2),
                new ResourceRequest(2, 0));
        List<ResourceRequest> noCycle = List.of(
                new ResourceRequest(0, 1),
                new ResourceRequest(1, 2));
        System.out.println("Wait-for graph cycle: " + detectCycle(3, cycle));
        System.out.println("Cycle-free graph: " + detectCycle(3, noCycle));
    }
}
