package com.gabrielgua.realworld.demo;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DemoService {
    private DemoClient demoClient;
    public void doSomething(String requestParam1, String requestParam2) {
        // We don't care about the result in this example
        demoClient.doPut(requestParam1, requestParam2);
        // Since we don't care about the result, and there is no checked exception we do nothing
        // Unchecked exceptions will propagate automatically to whatever layer is setup to handle exceptions
    }
}
