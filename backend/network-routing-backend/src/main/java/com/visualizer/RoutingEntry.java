package com.visualizer;

public class RoutingEntry {
    private final String destination;
    private String nextHop;
    private int cost;

    public RoutingEntry(String destination, String nextHop, int cost) {
        this.destination = destination;
        this.nextHop = nextHop;
        this.cost = cost;
    }

    public String getDestination() {
        return destination;
    }

    public String getNextHop() {
        return nextHop;
    }

    public int getCost() {
        return cost;
    }

    public void setNextHop(String nextHop) {
        this.nextHop = nextHop;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

   @Override
public String toString() {
    String costStr = (cost == Integer.MAX_VALUE) ? "∞" : String.valueOf(cost);
    return String.format("dest=%s, nextHop=%s, cost=%s", destination, nextHop, costStr);
}
}