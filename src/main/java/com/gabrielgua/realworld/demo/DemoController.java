package com.gabrielgua.realworld.demo;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/demo")
@AllArgsConstructor
public class DemoController {
    private DemoService service;
    public ResponseEntity<DemoResponse> doSomething(@RequestParam String requestParam1, String requestParam2) {
        service.doSomething(requestParam1, requestParam2);
        // We don't care about the result in this example
        // Since we don't care about the result, and there is no checked exception we do nothing
        // Unchecked exceptions will propagate automatically to whatever layer is setup to handle exceptions

        // Returning a standard response which is independent of the service call
        // If the body is blank you can use ResponseEntity.ok() instead
        return ResponseEntity.ok(new DemoResponse("Standard Message"));
    }
}
