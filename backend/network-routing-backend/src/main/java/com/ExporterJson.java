package com.visualizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ExporterJson {

    public static void exportToJson(Topology topo, String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Map<String, List<Map<String, Object>>> exportData = new LinkedHashMap<>();

        for (Router r : topo.getRouters().values()) {
            List<Map<String, Object>> entries = new ArrayList<>();
            for (RoutingEntry entry : r.getRoutingTable().values()) {
                Map<String, Object> obj = new LinkedHashMap<>();
                obj.put("destination", entry.getDestination());
                obj.put("nextHop", (entry.getNextHop() == null) ? "—" : entry.getNextHop());
                obj.put("cost", (entry.getCost() == Integer.MAX_VALUE) ? "∞" : entry.getCost());
                entries.add(obj);
            }
            exportData.put(r.getName(), entries);
        }

        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(exportData, writer);
            System.out.println("✅ Exported routing tables to " + filename);
        } catch (IOException e) {
            System.err.println("❌ Error exporting JSON: " + e.getMessage());
        }
    }
}