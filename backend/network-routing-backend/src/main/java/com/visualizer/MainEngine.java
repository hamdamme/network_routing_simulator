package com.visualizer;

public class MainEngine {
    public static void main(String[] args) throws Exception {
        Topology topo = Initializer.load("configs/router1.txt");

        for (Router r : topo.getRouters().values()) {
            System.out.println(r.getName() + " -> " + r.getNeighbors());
        }
    }
}