package com.visualizer.networkroutingservice;

import org.springframework.web.bind.annotation.*;
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
    public RunResponse runAlgorithm(@RequestBody RunRequest req) {
        RunResponse response = new RunResponse();
        response.algorithm = req.algorithm;

        // For now just echo dummy result (later we wire backend)
        Map<String, Object> row = new HashMap<>();
        row.put("destination", "R2");
        row.put("nextHop", "R2");
        row.put("cost", 140);

        response.tables.put("R1", List.of(row));

        return response;
    }
}