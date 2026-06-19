package com.jbd.integration;

import com.jbd.dto.AuthResponse;
import com.jbd.dto.LoginRequest;
import com.jbd.dto.RegisterRequest;
import com.jbd.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private UserRepository userRepository;

    private static final String TEST_USERNAME = "integration_auth_user";
    private static final String TEST_PASSWORD = "testpass123";
    private static final String TEST_ROLE = "ROLE_USER";

    @AfterEach
    void cleanup() {
        userRepository.findByUsername(TEST_USERNAME).ifPresent(userRepository::delete);
    }

    private RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate();
        rt.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(org.springframework.http.HttpStatusCode statusCode) {
                return false;
            }
        });
        return rt;
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private ResponseEntity<String> register(String username, String password, String role) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setRole(role);
        return restTemplate().postForEntity(baseUrl() + "/auth/register", request, String.class);
    }

    private ResponseEntity<AuthResponse> login(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        return restTemplate().postForEntity(baseUrl() + "/auth/login", request, AuthResponse.class);
    }

    @Test
    void register_returns201_forNewUser() {
        ResponseEntity<String> response = register(TEST_USERNAME, TEST_PASSWORD, TEST_ROLE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("User registered successfully");
    }

    @Test
    void register_returns409_whenUsernameAlreadyExists() {
        register(TEST_USERNAME, TEST_PASSWORD, TEST_ROLE);

        ResponseEntity<String> response = register(TEST_USERNAME, TEST_PASSWORD, TEST_ROLE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo("Username already exists");
    }

    @Test
    void login_returns200WithToken_forValidCredentials() {
        register(TEST_USERNAME, TEST_PASSWORD, TEST_ROLE);

        ResponseEntity<AuthResponse> response = login(TEST_USERNAME, TEST_PASSWORD);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isNotBlank();
    }
}