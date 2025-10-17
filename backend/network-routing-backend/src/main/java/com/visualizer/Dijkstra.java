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
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String routerName : topology.getRouters().keySet()) {
            dist.put(routerName, Integer.MAX_VALUE);
            prev.put(routerName, null);
        }
        dist.put(source.getName(), 0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(source.getName());

        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (visited.contains(u)) continue;
            visited.add(u);

            Router routerU = topology.getRouter(u);
            for (Map.Entry<String, Integer> edge : routerU.getNeighbors().entrySet()) {
                String v = edge.getKey();
                int weight = edge.getValue();
                if (dist.get(u) != Integer.MAX_VALUE && dist.get(u) + weight < dist.get(v)) {
                    dist.put(v, dist.get(u) + weight);
                    prev.put(v, u);
                    pq.add(v);
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