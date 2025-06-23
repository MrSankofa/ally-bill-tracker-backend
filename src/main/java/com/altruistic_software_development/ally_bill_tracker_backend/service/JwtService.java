package com.altruistic_software_development.ally_bill_tracker_backend.service;

import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final String jwtSecret = "supersecretkey123456789012345678901234"; // TODO: store in .env later

    // Token validity duration (1 hour in milliseconds)
    private long expiration = 1000 * 60 * 60; // 1 hour

    /**
     * Returns the secret signing key derived from your `jwtSecret` string.
     * Used to sign and validate JWTs.
     */
    public Key getSignInKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param email The unique user ID (UUID or DB ID)
     * @param roles  The set of roles granted to the user (e.g., USER, ADMIN)
     * @return A signed JWT as a compact string (header.payload.signature)
     *
     * Learning Note:
     * - The token is valid for 1 hour
     * - The token includes custom claims (`roles`)
     * - Signature algorithm: HS256 (HMAC using SHA-256)
     */
    public String generateToken(String email, Set<Role> roles) {

        Set<String> roleNames = roles.stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .setSubject(email) // user identifier (used later to load user details)
                .claim("roles", roleNames) // roles added as custom claims for RBAC
                .setIssuedAt(new Date()) //
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // expires in 1 hour
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // secure signature
                .compact(); // serialize token to string
    }

    /*Extracts the email (subject) from the JWT
    *
    * @Param token JWT token string
    * @Return the userId (as stored in setSubject
    * * */
    public String extractUsername(String token) {
        return getClaims(token).getBody().getSubject();
    }

    private Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT Token");
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = extractUsername(token);

        return (userId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = getClaims(token).getBody();

        System.out.println("Extracted roles: " + claims.get("roles"));

        return claims.get("roles", List.class);
    }

}
