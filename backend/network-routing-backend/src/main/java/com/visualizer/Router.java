package com.visualizer;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final String name;
    private final Map<String, Integer> neighbors = new HashMap<>();

    public Router(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public Map<String, Integer> getNeighbors() { return neighbors; }

    public void addNeighbor(String neighbor, int cost) {
        neighbors.put(neighbor, cost);
    }
}