
# JWT Security and Authentication Notes

## Core Topics to Study

### Beans and Spring Security

[//]: # (- **How are beans created in Spring and how are they used?**)

[//]: # (- **Why do we need a `SecurityConfig` configuration bean?**)

[//]: # (- **How does `SecurityConfig` work with the `JwtAuthenticationFilter`?**)

[//]: # (- **How does `securityFilterChain` intercept requests and check for authentication and validity?**)

### • How are beans created in Spring and how are they used?

Beans in Spring are created using annotations like `@Component`, `@Service`, `@Repository`, and registered through `@Configuration` classes with `@Bean` methods. They are managed by the Spring container and injected using `@Autowired`, `@Inject`, or constructor injection.

---

### • What is the UserDetails class by default?

`UserDetails` is an interface provided by Spring Security. It represents the core user information and includes methods such as `getUsername()`, `getPassword()`, and boolean flags like `isAccountNonLocked()`.

---

### • Why should we write our own UserDetails class?

You should create a custom `UserDetails` implementation when your user model (e.g. `User` entity) has additional fields or structure beyond what Spring Security expects. This allows your app to integrate domain-specific attributes and roles cleanly.

---

### • How does `BCryptPasswordEncoder` work?

It uses the BCrypt hashing function to securely encode passwords. It adds a random salt internally and applies multiple rounds of hashing, which makes brute-forcing more difficult.

---

### • Why do we need a `SecurityConfig` configuration bean?

It allows you to customize Spring Security's behavior, including defining secured endpoints, session policies, password encoders, and the use of custom filters like JWT authentication filters.

---

### • How does the `SecurityConfig` bean work with the `JwtAuthenticationFilter`?

It registers your filter using `.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)` so it can inspect incoming requests before the standard authentication process.

---

### • How does the `SecurityFilterChain` intercept our requests and check for authentication and validity?

The chain evaluates each registered filter in sequence. The JWT filter checks for a token, validates it, and sets the authenticated context if valid. If the filter chain doesn't find valid authentication, a 401 is returned.

---

### Spring Security Concepts

[//]: # (- **What is the `UserDetails` class by default?**)

[//]: # (- **Why should we write our own `UserDetails` class?**)

[//]: # (- **How does `BCryptPasswordEncoder` work?**)

[//]: # (- **How are sessions created?**)

[//]: # (- **What is the `UsernamePasswordAuthenticationFilter`?**)

[//]: # (- **What is the `SecurityContextHolder`?**)

### • How are sessions created?

Spring Security can create sessions to store authentication information. When using JWT (stateless), session creation is disabled using `.sessionCreationPolicy(SessionCreationPolicy.STATELESS)`.

---

### • What is the `UsernamePasswordAuthenticationFilter` class? How does it work with our request interception?

It processes login requests where credentials are submitted. When using JWT, you typically replace it with your own authentication mechanism and token validation filter.

---

### • How do you create a `JwtAuthenticationFilter`? Why do we need one?

You extend `OncePerRequestFilter` and override `doFilterInternal` to extract, validate, and authenticate a token. It's necessary for intercepting every request and authenticating via JWT instead of session cookies.

---

### • How do you keep the API paths DRY?

Use a constants class like `ApiPaths` that stores common prefixes (e.g. `/api/v1/auth`) and reuse them in controllers and security config.

---

### • Should we generate a token from the email or the userId?

Email is often used because it’s what the user knows and uses to log in. UserID can be used internally, but you'd need to look it up post-login. Either is valid if it's unique and constant.

---

### • How do you use TDD when implementing a jwtFilter?

TDD cycle: **Red → Green → Refactor**.

1. Write failing tests that assert expected behavior.
2. Implement code to pass the test.
3. Refactor for readability and design.

For JWT:
- Write a Spring Boot test that mocks a request with a token
- Generate a token with roles and verify parsing logic
- Use the signing key to decode and assert the claims

---

### • How does the token store the username and roles in itself?

JWT includes them in the payload. Using:
```java
Jwts.builder()
  .setSubject(email)
  .claim("roles", roles)
```
Spring decodes the payload and uses the claims during request validation.

---

### • How do you write a custom `UserDetails`?

Create a class that implements `UserDetails`. Map fields like username, password, roles, and implement the boolean methods (`isEnabled`, `isAccountNonLocked`, etc.)

---

### • When should we implement `isAccountNonLocked`, `isEnabled`, etc.?

When your app requires advanced user state management. These allow features like:
- Disabling accounts
- Locking out users after failed logins
- Marking accounts as expired

---

### • Break down what is involved in a `JwtAuthFilter`

- Read Authorization header
- Extract JWT
- Use `JwtService` to:
    - extract username (subject)
    - validate token (signature and expiration)
- Load user with `UserDetailsService`
- Create and set `UsernamePasswordAuthenticationToken` in `SecurityContext`

---

### • Understand what a claim is in authentication

Claims are key-value pairs included in the JWT payload that assert identity or roles of the user. For example: `"sub": "brett@example.com", "roles": ["USER"]`

---

### • How does `UserDetails` get the username and store it?

When authenticating, Spring loads the user from the database using the email or username. The `UserDetails` implementation returns it using `getUsername()`.

---

### • What is a filter chain?

It’s a sequence of filters (middleware) that a request passes through. Spring Security uses this chain to apply authentication, CSRF checks, and more.

---

### • What does JWT stand for?

**JSON Web Token**. It's a compact, URL-safe token format used to securely transmit information between parties.

---

### • What is authentication vs authorization?

- **Authentication**: Who are you? (e.g., login)
- **Authorization**: What are you allowed to do? (e.g., admin access)

---

### • What are authorities?

Authorities represent granular permissions (e.g., `READ_PRIVILEGES`). Roles are broad categories (e.g., `ROLE_ADMIN`) that can contain multiple authorities.

---

### • What is the `SecurityContextHolder`?

It holds the security context (including authentication) for the current request thread. It’s used by Spring Security to determine if a user is authenticated.

### JWT and Token Handling
- **Should you generate a token from the email or the user ID?**
    - Email is better for usability.
    - Backend can always look up the user ID from email if needed.
- **How does a JWT store the username and roles?**
    - Stored as claims using `.setSubject()` and `.claim("roles", roles)` in `generateToken`.

### TDD and JWT Implementation
- Use a TDD flow: **Red -> Green -> Refactor**
    - Write a failing test
    - Implement minimal logic to pass
    - Clean/refactor
- Testing JWT:
    - Use `@SpringBootTest`
    - Confirm the token includes email (subject) and roles (claims)
    - Parse token using `io.jsonwebtoken` to verify claims

### JWTService Responsibilities
- Store the signing key securely
- `generateToken(String email, Set<Role> roles)`
- `extractUsername(String token)`
- `isTokenValid(String token, UserDetails userDetails)`
- `isTokenExpired(String token)`

### JwtAuthenticationFilter Responsibilities
- Extend `OncePerRequestFilter`
- In `doFilterInternal()`:
    - Check if `Authorization` header exists and starts with `"Bearer "`
    - Extract JWT
    - Get username from JWT
    - Load user details from `UserDetailsService`
    - Validate token
    - If valid, set authentication in `SecurityContextHolder`

### UserDetailsService
- Provided by Spring Security
- Can be customized to connect to DB
- Loads `UserDetails` based on username/email

### RBAC and Authorities
- **RBAC**: Role-Based Access Control  — a system of assigning users roles which grant access to certain parts of the system.
- **Authorities**: Permissions tied to roles (e.g., `ROLE_ADMIN`, `ROLE_USER`)
- JWT stores roles as part of claims

---

## JWT Explained

### What is JWT?
- **JWT** stands for **JSON Web Token**
- A compact, URL-safe token that contains claims and is signed to ensure integrity

### JWT Structure
```
xxxxx.yyyyy.zzzzz
```

1. Header (Base64-encoded JSON):
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

2. Payload / Claims (Base64-encoded JSON):
```json
{
  "sub": "user@example.com",
  "roles": ["USER"],
  "exp": 1712345678
}
```

3. Signature:
- HMAC or RSA-based hash
- Ensures the token wasn’t tampered with
- Created using:
```text
HMAC_SHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secretKey)
```

### What Are Claims?
- Claims = statements about an entity (typically the user) and metadata
- Examples:
    - `"sub": "brett@example.com"`
    - `"roles": ["USER", "ADMIN"]`
    - `"exp": <timestamp>`

### JWT Analogy
Imagine a security guard at a door:

- You hand them a badge (JWT)
- The badge contains:
    - **sub**: identity
    - **roles**: permissions
    - **exp**: expiration time
- The guard checks:
    - Is it expired?
    - Is the signature valid?
    - Do you have permission?

---

## Common Questions

| Question | Answer |
|---|---|
| Is the payload private? | No. It's Base64-encoded, not encrypted |
| Is the signature what secures access? | Yes. It prevents tampering |
| How do users get a valid token? | By logging in via `/auth/login` |
| What happens if token is invalid/missing? | 401 Unauthorized |

---

## Bonus Notes

- **Base64 decoding** sites like base64decode.org can decode header and payload, not signature
- **Sensitive data** should never go in payload
- **If you need encryption**, use JWE (JSON Web Encryption)
