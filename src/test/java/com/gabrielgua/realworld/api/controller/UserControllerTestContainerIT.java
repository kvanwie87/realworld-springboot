package com.gabrielgua.realworld.api.controller;


import com.gabrielgua.realworld.domain.model.User;
import com.gabrielgua.realworld.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.junit.jupiter.Container;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @ActiveProfiles("it")
// @Testcontainers
public class UserControllerTestContainerIT {
    // Options for database in integration tests:
    // 1. Use an in-memory database like H2 - H2 is not always a perfect substitute for your SQL implementation, could have issues with flyway
    // 2. Use Testcontainers - will more closely replicate database but requires a Docker environment. Would be slower to startup tests, your CI would need to support Docker, your CI would need resources to run the testcontainer
        // Locally you can use Docker Desktop
        // GitLab CI has a docker-in-docker service you can use (see https://dotnet.testcontainers.org/cicd/)
    // 3. Use mocks for the repository layer - doesn't test the real database interactions, you would have to exclude your flyway migrations entirely, but your tests would be significantly faster

    //@MockBean
    private UserRepository userRepository;

    // @Test
    public void should() {
        User user = new User();
        user.setId(1L);
        user.setEmail("abc@gmail.com");
        user.setPassword("pass");
        user.setToken("tok");
        user.setProfile(null);
        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findById(1L);
        assert(fetchedUser.isPresent());
        assertEquals(user.getEmail(), fetchedUser.get().getEmail());
    }
}
