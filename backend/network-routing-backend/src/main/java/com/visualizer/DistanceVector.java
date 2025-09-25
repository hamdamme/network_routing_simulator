package com.visualizer;

import java.util.Map;

public class DistanceVector implements RoutingAlgorithm {
    @Override
    public void run(Topology topology) {
        boolean updated;
        do {
            updated = false;

            for (Router router : topology.getRouters().values()) {
                for (Map.Entry<String, Integer> nbEntry : router.getNeighbors().entrySet()) {
                    String neighborName = nbEntry.getKey();
                    Router neighbor = topology.getRouter(neighborName);

                    for (RoutingEntry nbEntryRt : neighbor.getRoutingTable().values()) {
                        String dest = nbEntryRt.getDestination();
                        int newCost = nbEntry.getValue() + nbEntryRt.getCost();

                        RoutingEntry current = router.getRoutingTable().get(dest);
                        if (newCost < current.getCost()) {
                            current.setCost(newCost);
                            current.setNextHop(neighborName);
                            updated = true;
                        }
                    }
                }
            }
        } while (updated);
    }
}