package com.altruistic_software_development.ally_bill_tracker_backend.service;

import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;
import java.util.Set;

/**
 * wtService generates signed JWT tokens that are used to authenticate users.
 *
 * Tokens contain:
 * - user ID (subject)
 * - roles (for access control)
 * - expiration timestamp
 *
 * @author Brett
 * @version 1.0
 */
@Service
public class JwtService {

    // This is your signing secret. In production, store it in a secure environment variable (.env or AWS Secrets Manager).
    private final String jwtSercret = "supersecretkey123456789012345678901234"; // TODO: store in .env later

    // Token validity duration (1 hour in milliseconds)
    private long expiration = 1000 * 60 * 60; // 1 hour

    /**
     * Returns the secret signing key derived from your `jwtSecret` string.
     * Used to sign and validate JWTs.
     */
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(jwtSercret.getBytes());
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param userId The unique user ID (UUID or DB ID)
     * @param roles  The set of roles granted to the user (e.g., USER, ADMIN)
     * @return A signed JWT as a compact string (header.payload.signature)
     *
     * Learning Note:
     * - The token is valid for 1 hour
     * - The token includes custom claims (`roles`)
     * - Signature algorithm: HS256 (HMAC using SHA-256)
     */
    public String generateToken(String userId, Set<Role> roles) {
        return Jwts.builder()
                .setSubject(userId) // user identifier (used later to load user details)
                .claim("roles", roles) // roles added as custom claims for RBAC
                .setIssuedAt(new Date()) //
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // expires in 1 hour
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // secure signature
                .compact(); // serialize token to string
    }
}
