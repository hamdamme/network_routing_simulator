package com.visualizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Initializer {

    public static Topology load(String filepath) throws IOException {
        Topology topology = new Topology();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Example line: R1:(R2,200);(R3,700);(R4,200);
                String[] parts = line.split(":");
                String routerName = parts[0].trim();

                Router router = topology.getRouter(routerName);
                if (router == null) {
                    router = new Router(routerName);
                    topology.addRouter(router);
                }

                if (parts.length > 1) {
                    String[] neighbors = parts[1].split(";");
                    for (String neighbor : neighbors) {
                        if (!neighbor.trim().isEmpty()) {
                            String n = neighbor.replaceAll("[()]", "").trim(); // R2,200
                            String[] pair = n.split(",");
                            String neighborName = pair[0].trim();
                            int cost = Integer.parseInt(pair[1].trim());

                            router.addNeighbor(neighborName, cost);

                            // Ensure neighbor router exists
                            Router nb = topology.getRouter(neighborName);
                            if (nb == null) {
                                nb = new Router(neighborName);
                                topology.addRouter(nb);
                            }
                        }
                    }
                }
            }
        }
        return topology;
    }
}