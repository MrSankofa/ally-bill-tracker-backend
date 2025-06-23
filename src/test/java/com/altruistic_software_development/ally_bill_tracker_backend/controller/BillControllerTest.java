package com.altruistic_software_development.ally_bill_tracker_backend.controller;

import com.altruistic_software_development.ally_bill_tracker_backend.config.ApiPaths;
import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import com.altruistic_software_development.ally_bill_tracker_backend.model.User;
import com.altruistic_software_development.ally_bill_tracker_backend.repository.UserRepository;
import com.altruistic_software_development.ally_bill_tracker_backend.service.JwtService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = "com.altruistic_software_development") // 👈 Add this if missing
class BillControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRejectAccessToUserRouteForAdminRole() throws Exception {
        User adminUser = User.builder()
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("password"))
                .roles(Set.of(Role.ADMIN)) // NOT Role.USER
                .build();

        userRepository.save(adminUser);
        String token = jwtService.generateToken(adminUser.getEmail(), adminUser.getRoles());

        mockMvc.perform(get(ApiPaths.BILLS_API + "/protected-route")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden()); // this proves @PreAuthorize("hasRole('USER')") is working
    }



    @Test
    void shouldRejectAccessWithoutToken() throws Exception {
        mockMvc.perform(get(ApiPaths.BILLS_API + "/protected-route"))
                .andExpect(status().isForbidden()); // or isUnauthorized() if anonymous is blocked
    }

    @Test
    void showAllowAccessToUserRouteForUserRole() throws Exception {
        User newUser = User.builder()
                .email("user@gmail.com")
                .password(passwordEncoder.encode("password"))
                .roles(Set.of(Role.USER))
                .build();

        User user = userRepository.save(newUser);

        String token = jwtService.generateToken(user.getEmail(), user.getRoles());

        mockMvc.perform(get(ApiPaths.BILLS_API + "/protected-route")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

    }



}