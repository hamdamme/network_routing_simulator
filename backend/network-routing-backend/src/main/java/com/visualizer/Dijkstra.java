package com.visualizer;

import java.util.*;

public class Dijkstra implements RoutingAlgorithm {

    @Override
    public void run(Topology topology) {
        for (Router source : topology.getRouters().values()) {
            runForRouter(source, topology);
        }
    }

    private void runForRouter(Router source, Topology topology) {
        // Distance map: node -> cost
        Map<String, Integer> dist = new HashMap<>();
        // Previous node map: node -> previous hop
        Map<String, String> prev = new HashMap<>();

        for (String routerName : topology.getRouters().keySet()) {
            dist.put(routerName, Integer.MAX_VALUE);
            prev.put(routerName, null);
        }
        dist.put(source.getName(), 0);

        // Min-heap (cost, node)
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
                Comparator.comparingInt(Map.Entry::getValue));
        pq.add(new AbstractMap.SimpleEntry<>(source.getName(), 0));

        while (!pq.isEmpty()) {
            String u = pq.poll().getKey();
            Router routerU = topology.getRouter(u);

            for (Map.Entry<String, Integer> edge : routerU.getNeighbors().entrySet()) {
                String v = edge.getKey();
                int weight = edge.getValue();
                int alt = dist.get(u) + weight;

                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.add(new AbstractMap.SimpleEntry<>(v, alt));
                }
            }
        }

        // Update routing table
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