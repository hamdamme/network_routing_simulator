
package com.visualizer.networkroutingservice;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class RoutingController {

    @PostMapping("/run")
    public Map<String, Object> runRouting(@RequestBody Map<String, String> request) {
        String algorithm = request.get("algorithm");
        String config = request.get("config");

        // For now, just echo input back
        Map<String, Object> response = new HashMap<>();
        response.put("algorithm", algorithm);
        response.put("config", config);
        response.put("status", "success - endpoint is live!");
        return response;
    }
}