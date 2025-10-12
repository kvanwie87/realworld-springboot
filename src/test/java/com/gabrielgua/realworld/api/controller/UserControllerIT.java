package com.gabrielgua.realworld.api.controller;



import com.gabrielgua.realworld.api.security.AuthUtils;
import com.gabrielgua.realworld.api.security.authorization.AuthorizationConfig;
import com.gabrielgua.realworld.domain.model.Profile;
import com.gabrielgua.realworld.domain.model.User;
import com.gabrielgua.realworld.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("it")
public class UserControllerIT {
    // Options for database in integration tests:
    // 1. Use an in-memory database like H2 - H2 is not always a perfect substitute for your SQL implementation, could have issues with flyway
    // 2. Use Testcontainers - will more closely replicate database but requires a Docker environment. Would be slower to startup tests, your CI would need to support Docker, your CI would need resources to run the testcontainer
        // Locally you can use Docker Desktop
        // GitLab CI has a docker-in-docker service you can use (see https://dotnet.testcontainers.org/cicd/)
    // 3. Use mocks for the repository layer - doesn't test the real database interactions, you would have to exclude your flyway migrations entirely, but your tests would be significantly faster

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AuthorizationConfig authorizationConfig;

    @MockitoBean
    private AuthUtils authUtils;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should() {
        User user = new User();
        user.setId(1L);
        user.setEmail("abc@gmail.com");
        user.setPassword("pass");
        user.setToken("tok");
        user.setProfile(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findById(1L);
        assert(fetchedUser.isPresent());
        assertEquals(user.getEmail(), fetchedUser.get().getEmail());
    }

    @Test
    public void shouldCallUserEndpoints() throws Exception {
        when(authorizationConfig.isAuthenticated()).thenReturn(true);
        when(authUtils.getCurrentUserEmail()).thenReturn("abc@gmail.com");
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser()));
        mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username", is("username")));
    }

    public User testUser() {
        User user = new User();
        user.setId(1L);
        user.setToken("tok");
        user.setEmail("abc@gmail.com");
        user.setPassword("pass");

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setId(1L);
        profile.setImage("img.png");
        profile.setBio("bio text");
        profile.setUsername("username");
        user.setProfile(profile);
        return user;
    }
}
