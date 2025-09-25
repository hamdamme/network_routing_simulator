package com.visualizer;

import java.util.*;

public class BellmanFord implements RoutingAlgorithm {

    @Override
    public void run(Topology topology) {
        for (Router source : topology.getRouters().values()) {
            runForRouter(source, topology);
        }
    }

    private void runForRouter(Router source, Topology topology) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        for (String routerName : topology.getRouters().keySet()) {
            dist.put(routerName, Integer.MAX_VALUE);
            prev.put(routerName, null);
        }
        dist.put(source.getName(), 0);

        int V = topology.getRouters().size();

        // Relax edges V-1 times
        for (int i = 0; i < V - 1; i++) {
            for (Router u : topology.getRouters().values()) {
                for (Map.Entry<String, Integer> edge : u.getNeighbors().entrySet()) {
                    String v = edge.getKey();
                    int weight = edge.getValue();

                    if (dist.get(u.getName()) != Integer.MAX_VALUE &&
                        dist.get(u.getName()) + weight < dist.get(v)) {
                        dist.put(v, dist.get(u.getName()) + weight);
                        prev.put(v, u.getName());
                    }
                }
            }
        }

        // Optional: check for negative cycles
        for (Router u : topology.getRouters().values()) {
            for (Map.Entry<String, Integer> edge : u.getNeighbors().entrySet()) {
                String v = edge.getKey();
                int weight = edge.getValue();
                if (dist.get(u.getName()) != Integer.MAX_VALUE &&
                    dist.get(u.getName()) + weight < dist.get(v)) {
                    System.out.println("Warning: Negative cycle detected!");
                }
            }
        }

        // Build routing table
        for (String dest : topology.getRouters().keySet()) {
            if (dest.equals(source.getName())) {
                source.getRoutingTable().put(dest,
                        new RoutingEntry(dest, source.getName(), 0));
            } else {
                String nextHop = findNextHop(dest, prev, source.getName());
                int cost = dist.get(dest);
                source.getRoutingTable().put(dest,
                        new RoutingEntry(dest, nextHop, cost));
            }
        }
    }

    private String findNextHop(String dest, Map<String, String> prev, String source) {
        String hop = dest;
        String parent = prev.get(hop);
        while (parent != null && !parent.equals(source)) {
            hop = parent;
            parent = prev.get(hop);
        }
        return hop;
    }
}