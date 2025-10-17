package com.visualizer;

import java.util.HashMap;
import java.util.Map;

public class Topology {
    private final Map<String, Router> routers = new HashMap<>();

    public void addRouter(Router router) {
        routers.put(router.getName(), router);
    }

    public Router getRouter(String name) {
        return routers.get(name);
    }

    public Map<String, Router> getRouters() {
        return routers;
    }
}