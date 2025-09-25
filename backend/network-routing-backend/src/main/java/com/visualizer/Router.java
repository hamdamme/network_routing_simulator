private final Map<String, RoutingEntry> routingTable = new HashMap<>();

public Map<String, RoutingEntry> getRoutingTable() { return routingTable; }

public void initializeRoutingTable(Topology topology) {
    for (String dest : topology.getRouters().keySet()) {
        if (dest.equals(this.name)) {
            routingTable.put(dest, new RoutingEntry(dest, this.name, 0));
        } else if (neighbors.containsKey(dest)) {
            routingTable.put(dest, new RoutingEntry(dest, dest, neighbors.get(dest)));
        } else {
            routingTable.put(dest, new RoutingEntry(dest, null, Integer.MAX_VALUE));
        }
    }
}