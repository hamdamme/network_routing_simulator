package com.visualizer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Exporter {

    public static void exportToCSV(Topology topo, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("Router,Destination,NextHop,Cost\n");
            for (Router r : topo.getRouters().values()) {
                for (RoutingEntry entry : r.getRoutingTable().values()) {
                    String costStr = (entry.getCost() == Integer.MAX_VALUE) ? "∞" : String.valueOf(entry.getCost());
                    writer.append(r.getName()).append(",")
                          .append(entry.getDestination()).append(",")
                          .append(entry.getNextHop() == null ? "—" : entry.getNextHop()).append(",")
                          .append(costStr).append("\n");
                }
            }
        }
        System.out.println("Exported CSV: " + filename);
    }

    public static void exportToJSON(Topology topo, String filename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(gson.toJson(topoToMap(topo)));
        }
        System.out.println("Exported JSON: " + filename);
    }

    private static Object topoToMap(Topology topo) {
        // Convert to a serializable structure
        return topo.getRouters().values().stream().collect(
            java.util.stream.Collectors.toMap(
                Router::getName,
                r -> r.getRoutingTable().values().stream().map(entry -> Map.of(
                    "destination", entry.getDestination(),
                    "nextHop", entry.getNextHop() == null ? "—" : entry.getNextHop(),
                    "cost", entry.getCost() == Integer.MAX_VALUE ? "∞" : entry.getCost()
                )).toArray()
            )
        );
    }
}