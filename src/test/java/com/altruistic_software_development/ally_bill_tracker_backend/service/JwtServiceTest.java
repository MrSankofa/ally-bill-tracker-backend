package com.altruistic_software_development.ally_bill_tracker_backend.service;

import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    public void testGenerateTokenIncludesUserIdAndRoles() {
        String token = jwtService.generateToken("abc123", Set.of(Role.USER, Role.ADMIN));
        assertNotNull(token);
    }

    @Test
    void testTokenContainsClaims() {
        String userId = "abc123";
        Set<Role> roles = Set.of(Role.USER, Role.ADMIN);
        String token = jwtService.generateToken(userId, roles);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("supersecretkey123456789012345678901234".getBytes())) // Use same key
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(userId, claims.getSubject());
        assertTrue(((List<?>) claims.get("roles")).contains("USER"));
        assertTrue(((List<?>) claims.get("roles")).contains("ADMIN"));
    }
}