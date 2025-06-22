Authentication & JWT Setup
1. JWT Token Generation
   Implemented JwtService to:

Sign tokens with user email as the subject (setSubject(email)).

Embed user roles as claims.

Set expiration (1 hour).

Validate tokens (isTokenValid, isTokenExpired).

2. Filter Requests with JWT
   Created JwtAuthenticationFilter (extends OncePerRequestFilter) to:

Extract token from Authorization header.

Use JwtService.extractUsername() to get email.

Look up UserDetails by email using UserDetailsService.

If valid, set the SecurityContext.

3. Security Configuration
   Set up SecurityConfig to:

Disable CSRF (for stateless APIs).

Add JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter.

Allow unauthenticated access to /api/v1/auth/**.

Require authentication for /api/v1/bills/**.

4. Refactored for DRY Routing
   Created ApiPaths constants:

public static final String AUTH_API = "/api/v1/auth";

public static final String BILLS_API = "/api/v1/bills";

Used these in controllers and tests to prevent hardcoded paths.
UserDetails Integration
5. UserDetailsService Bean
   Implemented in AppConfig:

```java
@Bean
public UserDetailsService userDetailsService() {
return email -> userRepository.findByEmail(email)
.orElseThrow(() -> new UsernameNotFoundException("User not found"));
}

```
Ensures Spring Security uses your actual user data from DB rather than in-memory.

Protected Endpoint & Test
6. Created Test Route
   In BillController:

```java
@GetMapping("/protected-route")
public ResponseEntity<?> getProtectedRoute() {
    return ResponseEntity.ok().build();
}

```
7. Integration Test
   Wrote MockMvc test that:

Logs in as test@example.com

Generates a valid token with JwtService

Sends GET request to /api/v1/bills/protected-route

Expects HTTP 200 OK

Fixed issue with hardcoded path by using:

```
get(ApiPaths.BILLS_API + "/protected-route");
```

Debugged Issues Along the Way
Fixed a test failure due to:

Token subject being email, but test user wasn’t found (UsernameNotFoundException)

UserDetailsService expecting a return type of UserDetails (added cast or implementation as needed)

MockMvc test hitting a static resource handler due to incorrect route

