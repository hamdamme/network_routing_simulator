package com.visualizer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class GraphExporter {

    public static void exportToDot(Topology topo, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("graph Network {\n");
            writer.write("  node [shape=circle, style=filled, color=lightblue];\n");

            // export edges
            for (Router r : topo.getRouters().values()) {
                for (Map.Entry<String, Integer> edge : r.getNeighbors().entrySet()) {
                    String neighbor = edge.getKey();
                    int cost = edge.getValue();

                    // undirected graph, so only write edge once
                    if (r.getName().compareTo(neighbor) < 0) {
                        writer.write("  " + r.getName() + " -- " + neighbor +
                                     " [label=\"" + cost + "\"];\n");
                    }
                }
            }

            writer.write("}\n");
            System.out.println("✅ Exported network topology to DOT file: " + filename);
        } catch (IOException e) {
            System.err.println("❌ Error writing DOT file: " + e.getMessage());
        }
    }
}