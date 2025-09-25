package com.visualizer;

public class MainEngine {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java MainEngine <algorithm> <config-file>");
            System.out.println("Algorithms: dv | dijkstra | bf");
            return;
        }

        String algoName = args[0].toLowerCase();
        String configFile = args[1];

        // Load topology
        Topology topo = Initializer.load(configFile);

        // Initialize routing tables
        for (Router r : topo.getRouters().values()) {
            r.initializeRoutingTable(topo);
        }

        // Pick algorithm
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

        // Run algorithm
        algo.run(topo);

        // Print results
        for (Router r : topo.getRouters().values()) {
            System.out.println("Router " + r.getName());
            r.getRoutingTable().values().forEach(System.out::println);
            System.out.println("-----");
        }
    }
}