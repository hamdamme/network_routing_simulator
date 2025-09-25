package com.visualizer;
public static void main(String[] args) throws Exception {
    Topology topo = Initializer.load("configs/router1.txt");

    // initialize tables
    for (Router r : topo.getRouters().values()) {
        r.initializeRoutingTable(topo);
    }

    // Run chosen algorithm
    RoutingAlgorithm algo = new Dijkstra();  // or new DistanceVector()
    algo.run(topo);

    // Print results
    for (Router r : topo.getRouters().values()) {
        System.out.println("Router " + r.getName());
        r.getRoutingTable().values().forEach(System.out::println);
        System.out.println("-----");
    }
}
