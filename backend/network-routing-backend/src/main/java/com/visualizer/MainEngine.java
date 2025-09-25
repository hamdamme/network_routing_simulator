package com.visualizer;
public class MainEngine {
    public static void main(String[] args) throws Exception {
        Topology topo = Initializer.load("configs/router1.txt");

        for (Router r : topo.getRouters().values()) {
            r.initializeRoutingTable(topo);
        }

        // Pick algorithm
        RoutingAlgorithm algo = new BellmanFord(); // try DistanceVector, Dijkstra, or BellmanFord
        algo.run(topo);

        // Print results
        for (Router r : topo.getRouters().values()) {
            System.out.println("Router " + r.getName());
            r.getRoutingTable().values().forEach(System.out::println);
            System.out.println("-----");
        }
    }
}