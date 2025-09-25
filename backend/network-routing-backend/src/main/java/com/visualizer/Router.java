public void displayTable() {
    System.out.println("===== Routing Table for " + name + " =====");
    System.out.printf("%-12s %-10s %-5s%n", "Destination", "Next Hop", "Cost");
    System.out.println("----------- ---------- -----");

    for (RoutingEntry entry : routingTable.values()) {
        String nextHop = entry.getNextHop() == null ? "-" : entry.getNextHop();
        String cost = (entry.getCost() == Integer.MAX_VALUE) ? "∞" : String.valueOf(entry.getCost());
        System.out.printf("%-12s %-10s %-5s%n", entry.getDestination(), nextHop, cost);
    }
    System.out.println();
}