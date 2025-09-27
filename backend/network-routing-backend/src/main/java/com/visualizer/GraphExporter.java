package com.visualizer;

import java.io.FileWriter;
import java.io.IOException;

public class GraphExporter {

    public static void exportToDot(Topology topo, String baseFilename) {
        String dotFile = baseFilename + ".dot";
        String svgFile = baseFilename + ".svg";
        String pngFile = baseFilename + ".png";

        // Step 1: Write DOT file
        try (FileWriter writer = new FileWriter(dotFile)) {
            writer.write("graph Network {\n");
            writer.write("  node [shape=circle, style=filled, color=lightblue];\n");

            for (Router r : topo.getRouters().values()) {
                for (var edge : r.getNeighbors().entrySet()) {
                    String neighbor = edge.getKey();
                    int cost = edge.getValue();

                    // undirected graph, write edge once
                    if (r.getName().compareTo(neighbor) < 0) {
                        writer.write("  " + r.getName() + " -- " + neighbor +
                                     " [label=\"" + cost + "\"];\n");
                    }
                }
            }

            writer.write("}\n");
            System.out.println("✅ Exported DOT file: " + dotFile);
        } catch (IOException e) {
            System.err.println("❌ Error writing DOT file: " + e.getMessage());
            return;
        }

        // Step 2: Run Graphviz (dot -> SVG and PNG)
        try {
            Process svgProc = new ProcessBuilder("dot", "-Tsvg", dotFile, "-o", svgFile).start();
            svgProc.waitFor();
            System.out.println("✅ Exported SVG: " + svgFile);

            Process pngProc = new ProcessBuilder("dot", "-Tpng", dotFile, "-o", pngFile).start();
            pngProc.waitFor();
            System.out.println("✅ Exported PNG: " + pngFile);

        } catch (IOException | InterruptedException e) {
            System.err.println("⚠️ Graphviz not installed or failed to run: " + e.getMessage());
        }
    }
}