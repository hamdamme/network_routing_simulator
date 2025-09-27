package com.visualizer;

import java.io.FileWriter;
import java.io.IOException;

public class Exporter {

    public static void exportToCSV(Topology topo, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Router,Destination,NextHop,Cost\n");
            for (Router r : topo.getRouters().values()) {
                for (RoutingEntry entry : r.getRoutingTable().values()) {
                    String dest = entry.getDestination();
                    String nextHop = (entry.getNextHop() == null) ? "—" : entry.getNextHop();
                    String costStr = (entry.getCost() == Integer.MAX_VALUE) ? "∞" : String.valueOf(entry.getCost());
                    writer.write(r.getName() + "," + dest + "," + nextHop + "," + costStr + "\n");
                }
            }
            System.out.println("✅ Exported routing tables to " + filename);
        } catch (IOException e) {
            System.err.println("❌ Error exporting CSV: " + e.getMessage());
        }
    }
}