package com.visualizer;

import java.util.*;

public class DistanceVector implements RoutingAlgorithm {

    @Override
    public void run(Topology topology) {
        boolean updated;
        int iterations = 0;

        // Keep looping until no changes
        do {
            updated = false;
            iterations++;

            for (Router router : topology.getRouters().values()) {
                Map<String, RoutingEntry> table = router.getRoutingTable();

                // Look at each neighbor
                for (Map.Entry<String, Integer> neighborEntry : router.getNeighbors().entrySet()) {
                    String neighborName = neighborEntry.getKey();
                    int costToNeighbor = neighborEntry.getValue();

                    Router neighbor = topology.getRouter(neighborName);
                    if (neighbor == null) continue;

                    // Look at neighbor's routing table
                    for (RoutingEntry neighborRoute : neighbor.getRoutingTable().values()) {
                        String dest = neighborRoute.getDestination();
                        int newCost = (neighborRoute.getCost() == Integer.MAX_VALUE)
                                ? Integer.MAX_VALUE
                                : costToNeighbor + neighborRoute.getCost();

                        RoutingEntry current = table.get(dest);
                        if (current == null || newCost < current.getCost()) {
                            table.put(dest, new RoutingEntry(dest, neighborName, newCost));
                            updated = true;
                        }
                    }
                }
            }
        } while (updated && iterations < 100); // prevent infinite loops
    }
}