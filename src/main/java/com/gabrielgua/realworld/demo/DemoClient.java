package com.gabrielgua.realworld.demo;

import org.springframework.stereotype.Component;

@Component
public class DemoClient {
    public DemoClientResult doPut(String requestParam1, String requestParam2) {
        // Simulate a PUT request to an external service
        return new DemoClientResult();
    }
}
