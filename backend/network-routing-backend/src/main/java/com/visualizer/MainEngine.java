public static void main(String[] args) throws Exception {
    Topology topo = Initializer.load("configs/router1.txt");

    // initialize tables
    for (Router r : topo.getRouters().values()) {
        r.initializeRoutingTable(topo);
    }

    // run DV
    DistanceVector dv = new DistanceVector();
    dv.run(topo);

    // print results
    for (Router r : topo.getRouters().values()) {
        System.out.println("Router " + r.getName());
        r.getRoutingTable().values().forEach(System.out::println);
        System.out.println("-----");
    }
}