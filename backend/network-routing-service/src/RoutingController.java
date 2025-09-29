package com.visualizer;

import org.springframework.web.bind.annotation.*;
import java.nio.file.*;
import java.util.*;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class RoutingController {

    @PostMapping("/run")
    public Map<String, Object> run(@RequestBody Map<String, String> body) throws Exception {
        String algorithm = Optional.ofNullable(body.get("algorithm")).orElse("dijkstra").toLowerCase();
        String configText = body.get("config");
        String configPath = body.get("configPath");

        if ((configText == null || configText.isBlank()) && (configPath == null || configPath.isBlank())) {
            throw new IllegalArgumentException("Provide either 'config' (text) or 'configPath' (file path).");
        }

        Path tmp = null;
        try {
            String pathToUse;
            if (configText != null && !configText.isBlank()) {
                tmp = Files.createTempFile("topology", ".txt");
                Files.writeString(tmp, configText);
                pathToUse = tmp.toString();
            } else {
                pathToUse = configPath;
            }

            Topology topo = Initializer.load(pathToUse);
            for (Router r : topo.getRouters().values()) r.initializeRoutingTable(topo);

            RoutingAlgorithm algo = switch (algorithm) {
                case "dv" -> new DistanceVector();
                case "bf" -> new BellmanFord();
                default -> new Dijkstra();
            };
            algo.run(topo);

            Map<String, Object> tables = new LinkedHashMap<>();
            for (Router r : topo.getRouters().values()) {
                List<Map<String, Object>> rows = new ArrayList<>();
                for (RoutingEntry e : r.getRoutingTable().values()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("destination", e.getDestination());
                    row.put("nextHop", e.getNextHop() == null ? "—" : e.getNextHop());
                    row.put("cost", e.getCost() == Integer.MAX_VALUE ? "∞" : e.getCost());
                    rows.add(row);
                }
                tables.put(r.getName(), rows);
            }

            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("algorithm", algorithm);
            resp.put("tables", tables);
            return resp;

        } finally {
            if (tmp != null) try { Files.deleteIfExists(tmp); } catch (IOException ignored) {}
        }
    }
}