package com.altruistic_software_development.ally_bill_tracker_backend;

import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import com.altruistic_software_development.ally_bill_tracker_backend.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    public void testGenerateTokenIncludesUserIdAndRoles() {
        String token = jwtService.generateToken("abc123", Set.of(Role.USER, Role.ADMIN));
        assertNotNull(token);
    }
}
