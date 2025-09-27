package com.visualizer;

public class MainEngine {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java MainEngine <algorithm|all> <config-file>");
            System.out.println("Algorithms: dv | dijkstra | bf | all");
            System.out.println("Example: mvn exec:java -Dexec.mainClass=\"com.visualizer.MainEngine\" -Dexec.args=\"all ../../configs/router1.txt\" -Dexport=output");
            return;
        }

        String algoName = args[0].toLowerCase();
        String configFile = args[1];

        if (algoName.equals("all")) {
            runAlgorithm("Distance Vector", new DistanceVector(), configFile);
            runAlgorithm("Dijkstra", new Dijkstra(), configFile);
            runAlgorithm("Bellman-Ford", new BellmanFord(), configFile);
        } else {
            RoutingAlgorithm algo;
            switch (algoName) {
                case "dv":
                    algo = new DistanceVector();
                    break;
                case "dijkstra":
                    algo = new Dijkstra();
                    break;
                case "bf":
                    algo = new BellmanFord();
                    break;
                default:
                    System.out.println("Unknown algorithm: " + algoName);
                    return;
            }
            runAlgorithm(algoName.toUpperCase(), algo, configFile);
        }
    }

    private static void runAlgorithm(String name, RoutingAlgorithm algo, String configFile) throws Exception {
        System.out.println("=== " + name + " ===");
        Topology topo = Initializer.load(configFile);

        // initialize tables
        for (Router r : topo.getRouters().values()) {
            r.initializeRoutingTable(topo);
        }

        // run chosen algorithm
        algo.run(topo);

        // pretty-print routing tables
        for (Router r : topo.getRouters().values()) {
            System.out.println("Router " + r.getName());
            System.out.println(String.format("%-12s %-12s %-6s", "Destination", "NextHop", "Cost"));
            System.out.println("-----------------------------------");
            for (RoutingEntry entry : r.getRoutingTable().values()) {
                String dest = entry.getDestination();
                String nextHop = (entry.getNextHop() == null) ? "—" : entry.getNextHop();
                String costStr = (entry.getCost() == Integer.MAX_VALUE) ? "∞" : String.valueOf(entry.getCost());
                System.out.println(String.format("%-12s %-12s %-6s", dest, nextHop, costStr));
            }
            System.out.println();
        }

        // export if user asked
        if (System.getProperty("export") != null) {
            String filename = System.getProperty("export") + "_" + name.replace(" ", "_") + ".csv";
            Exporter.exportToCSV(topo, filename);
        }

        System.out.println();
    }
}