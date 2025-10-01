package com.visualizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class Initializer {

    // Load from a file path
    public static Topology load(String filepath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            return parse(br);
        }
    }

    // Load from a config string (for API input)
    public static Topology loadFromString(String configText) throws IOException {
        try (BufferedReader br = new BufferedReader(new StringReader(configText))) {
            return parse(br);
        }
    }

    // Shared parsing logic
    private static Topology parse(BufferedReader br) throws IOException {
        Topology topology = new Topology();

        // ✅ Skip the first line (number of routers)
        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            // Example line: R1: (R2, 140), (R4, 180)
            String[] parts = line.split(":");
            String routerName = parts[0].trim();

            Router router = topology.getRouter(routerName);
            if (router == null) {
                router = new Router(routerName);
                topology.addRouter(router);
            }

            if (parts.length > 1) {
                for (String neighbor : parts[1].split("\\)")) {
                    if (!neighbor.trim().isEmpty()) {
                        String n = neighbor.replaceAll("[()]", "").trim(); // e.g. R2,140
                        String[] pair = n.split(",");
                        if (pair.length == 2) {
                            String neighborName = pair[0].trim();
                            int cost = Integer.parseInt(pair[1].trim());

                            router.addNeighbor(neighborName, cost);

                            // Ensure neighbor exists in topology
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