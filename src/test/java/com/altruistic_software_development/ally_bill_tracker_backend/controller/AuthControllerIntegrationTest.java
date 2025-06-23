package com.altruistic_software_development.ally_bill_tracker_backend.controller;

import org.junit.jupiter.api.BeforeEach;
import com.altruistic_software_development.ally_bill_tracker_backend.config.ApiPaths;
import com.altruistic_software_development.ally_bill_tracker_backend.dto.AuthRequest;
import com.altruistic_software_development.ally_bill_tracker_backend.dto.RegisterRequest;
import com.altruistic_software_development.ally_bill_tracker_backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// TODO:
/*
Write integration tests for:

 Successful registration

 Duplicate registration

 Successful login

 Failed login (wrong password)

 Confirm JWT is returned on successful login

 ensure DB is clean between tests
* */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    private final String email = "testuser@example.com";
    private final String password = "securePassword123";
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);

        mockMvc.perform(post(ApiPaths.AUTH_API + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @Order(2)
    void shouldNotRegisterDuplicateUser() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);
        userRepository.save(request.toUser(passwordEncoder)); // assuming you have a toUser() helper method or similar

        mockMvc.perform(post(ApiPaths.AUTH_API + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("already exists")));
    }

    @Test
    @Order(3)
    void shouldLoginUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);
        mockMvc.perform(post(ApiPaths.AUTH_API + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        AuthRequest AuthRequest = new AuthRequest();
        AuthRequest.setEmail(email);
        AuthRequest.setPassword(password);

        mockMvc.perform(post(ApiPaths.AUTH_API + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AuthRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @Order(4)
    void shouldRejectLoginWithInvalidPassword() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);
        mockMvc.perform(post(ApiPaths.AUTH_API + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        AuthRequest authReq = new AuthRequest();
        authReq.setEmail(email);
        authReq.setPassword("wrongPassword");

        mockMvc.perform(post(ApiPaths.AUTH_API + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authReq)))
                .andExpect(status().isUnauthorized());
    }
}