package com.jbd.integration;

import com.jbd.dto.AuthResponse;
import com.jbd.dto.LoginRequest;
import com.jbd.dto.RegisterRequest;
import com.jbd.entity.Employee;
import com.jbd.repository.EmployeeRepository;
import com.jbd.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final String TEST_USERNAME = "integration_emp_user";
    private static final String TEST_PASSWORD = "testpass123";

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        if (userRepository.findByUsername(TEST_USERNAME).isEmpty()) {
            RegisterRequest request = new RegisterRequest();
            request.setUsername(TEST_USERNAME);
            request.setPassword(TEST_PASSWORD);
            request.setRole("ROLE_USER");
            restTemplate().postForEntity(baseUrl() + "/auth/register", request, String.class);
        }
        testEmployee = employeeRepository.save(new Employee(0, "Integration Employee", 30));
    }

    @AfterEach
    void cleanup() {
        userRepository.findByUsername(TEST_USERNAME).ifPresent(userRepository::delete);
        if (testEmployee != null) {
            employeeRepository.deleteById(testEmployee.getEmployeeId());
        }
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

    private String getToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername(TEST_USERNAME);
        request.setPassword(TEST_PASSWORD);
        ResponseEntity<AuthResponse> response = restTemplate()
                .postForEntity(baseUrl() + "/auth/login", request, AuthResponse.class);
        return response.getBody().getToken();
    }

    @Test
    void getEmployees_returns401_whenNoTokenProvided() {
        ResponseEntity<String> response = restTemplate()
                .getForEntity(baseUrl() + "/employee/api/v1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getEmployees_returns401_whenTokenIsInvalid() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer this.is.not.valid");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate()
                .exchange(baseUrl() + "/employee/api/v1", HttpMethod.GET, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getEmployees_returns200_whenValidTokenProvided() {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate()
                .exchange(baseUrl() + "/employee/api/v1", HttpMethod.GET, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void fullFlow_registerLoginAndAccessProtectedEndpoint() {
        String uniqueUser = "full_flow_user_" + System.currentTimeMillis();

        // Register
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(uniqueUser);
        registerRequest.setPassword("mypassword");
        registerRequest.setRole("ROLE_USER");
        ResponseEntity<String> registerResponse = restTemplate()
                .postForEntity(baseUrl() + "/auth/register", registerRequest, String.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Login → get token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(uniqueUser);
        loginRequest.setPassword("mypassword");
        ResponseEntity<AuthResponse> loginResponse = restTemplate()
                .postForEntity(baseUrl() + "/auth/login", loginRequest, AuthResponse.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = loginResponse.getBody().getToken();

        // Access protected endpoint with token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<String> protectedResponse = restTemplate()
                .exchange(baseUrl() + "/employee/api/v1", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertThat(protectedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Cleanup
        userRepository.findByUsername(uniqueUser).ifPresent(userRepository::delete);
    }
}