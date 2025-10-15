package com.visualizer.networkroutingservice;

import com.visualizer.*;  // ✅ import your backend classes
import org.springframework.web.bind.annotation.*;

import java.util.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ApiController {

    // Request model
    public static class RunRequest {
        public String algorithm;
        public String config;
    }

    // Response model
    public static class RunResponse {
        public String algorithm;
        public Map<String, List<Map<String, Object>>> tables = new HashMap<>();
    }

    @PostMapping("/run")
    public RunResponse runAlgorithm(@RequestBody RunRequest req) throws Exception {
        RunResponse response = new RunResponse();
        response.algorithm = req.algorithm;

        // ✅ Load topology from config string
        Topology topo = Initializer.loadFromString(req.config);

        // ✅ Initialize all routers
        for (Router r : topo.getRouters().values()) {
            r.initializeRoutingTable(topo);
        }

        // ✅ Select and run algorithm
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
        algo.run(topo);

        // ✅ Convert routing tables into JSON-friendly format
        for (Router r : topo.getRouters().values()) {
            List<Map<String, Object>> rows = new ArrayList<>();
            for (RoutingEntry entry : r.getRoutingTable().values()) {
                Map<String, Object> row = new HashMap<>();
                row.put("destination", entry.getDestination());
                row.put("nextHop", (entry.getNextHop() == null) ? "—" : entry.getNextHop());
                row.put("cost", (entry.getCost() == Integer.MAX_VALUE) ? "∞" : entry.getCost());
                rows.add(row);
            }
            response.tables.put(r.getName(), rows);
        }

        return response;
    }
}