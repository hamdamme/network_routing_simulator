package com.visualizer.networkroutingservice;

import org.springframework.web.bind.annotation.*;

import com.visualizer.BellmanFord;
import com.visualizer.Dijkstra;
import com.visualizer.DistanceVector;
import com.visualizer.Initializer;
import com.visualizer.Router;
import com.visualizer.RoutingAlgorithm;
import com.visualizer.RoutingEntry;
import com.visualizer.Topology;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    // Simple request model
    public static class RunRequest {
        public String algorithm;
        public String config;
    }

    // Simple response model
    public static class RunResponse {
        public String algorithm;
        public Map<String, List<Map<String, Object>>> tables = new HashMap<>();
    }

   @PostMapping("/run")
public RunResponse runAlgorithm(@RequestBody RunRequest req) throws Exception {
    RunResponse response = new RunResponse();
    response.algorithm = req.algorithm;

    // Load topology from config string
    Topology topo = Initializer.loadFromString(req.config);

    // Choose algorithm
    RoutingAlgorithm algo;
    switch (req.algorithm.toLowerCase()) {
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
            throw new IllegalArgumentException("Unknown algorithm: " + req.algorithm);
    }

    // Initialize routing tables
    for (Router r : topo.getRouters().values()) {
        r.initializeRoutingTable(topo);
    }

    // Run algorithm
    algo.run(topo);

    // Collect results
    for (Router r : topo.getRouters().values()) {
        List<Map<String, Object>> table = new ArrayList<>();
        for (RoutingEntry entry : r.getRoutingTable().values()) {
            Map<String, Object> row = new HashMap<>();
            row.put("destination", entry.getDestination());
            row.put("nextHop", entry.getNextHop() == null ? "—" : entry.getNextHop());
            row.put("cost", entry.getCost() == Integer.MAX_VALUE ? "∞" : entry.getCost());
            table.add(row);
        }
        response.tables.put(r.getName(), table);
    }

    return response;
}

}