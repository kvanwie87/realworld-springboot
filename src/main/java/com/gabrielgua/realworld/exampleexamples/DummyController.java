package com.gabrielgua.realworld.exampleexamples;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DummyController {
    /*
    Ways to handle exceptions in Spring Boot:
    1. Unhandled exceptions by default get routed to BaseErrorController
       which returns a 500 Internal Server Error response.
    2. Use @ExceptionHandler in the controller to handle specific exceptions.
    3. Use @ControllerAdvice to handle exceptions globally across all controllers.
       - This uses @ExceptionHandler as well
    4. Try catch blocks in the controller methods to catch exceptions and return a custom response.


    I find @ControllerAdvice to be the most elegant solution for handling exceptions globally.
    And you typically still need try catch blocks in your service methods to handle specific cases, log errors and rethrow exceptions as custom exceptions.

    When constructing real error messages you likely want to use a custom error response object and builder class to construct it.

     */
    private DummyService dummyService;

    @GetMapping("/dummy")
    public ResponseEntity<String> dummy() {
        try {
            String message = dummyService.getDummyMessage();
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) { // This method could be put inside a @ControllerAdvice class or in individual controllers
        return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
    }
}
