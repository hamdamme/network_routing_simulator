package com.visualizer;

public class MainEngine {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java MainEngine <algorithm|all> <config-file>");
            System.out.println("Algorithms: dv | dijkstra | bf | all");
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

        // print results
        for (Router r : topo.getRouters().values()) {
            System.out.println("Router " + r.getName());
            r.getRoutingTable().values().forEach(System.out::println);
            System.out.println("-----");
        }
        System.out.println();
    }
}