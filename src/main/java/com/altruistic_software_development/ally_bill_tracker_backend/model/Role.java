package com.altruistic_software_development.ally_bill_tracker_backend.model;

/*
*
* Role-Based Access Control is a security model that restricts access to parts of your application based on a user’s role.

Instead of checking "who" the user is, you check "what role" they have — and you allow or deny actions based on that.
*
*
* In a Spring Security JWT App
You:

Assign roles when users are created

Encode the role(s) into their JWT

*
*
* Do both security config bean and preAuthorized annotations on the routes.
*
Use Spring Security to restrict access to endpoints
.antMatchers("/api/admin/**").hasRole("ADMIN")
.antMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")

* // Admin-only access
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/api/admin/users")
public List<User> getAllUsers() { ... }

// Shared access
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@GetMapping("/api/dashboard")
public DashboardData getDashboard() { ... }
* */
public enum Role {
    USER,
    ADMIN,
}
