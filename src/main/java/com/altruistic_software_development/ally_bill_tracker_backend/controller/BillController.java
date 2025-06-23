package com.altruistic_software_development.ally_bill_tracker_backend.controller;

import com.altruistic_software_development.ally_bill_tracker_backend.config.ApiPaths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.BILLS_API)
public class BillController {

//    @GetMapping("/protected-route")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<?> getProtectedRoute() {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Current Auth: " + auth);
//        return ResponseEntity.ok("You should not see this with ADMIN");
//    }

    @GetMapping("/protected-route")
    public ResponseEntity<?> getProtectedRoute() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("Authenticated: " + auth.isAuthenticated());
        System.out.println("Authorities: " + auth.getAuthorities());

        boolean hasUserRole = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_USER"));

        if (!hasUserRole) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: USER role required");
        }

        return ResponseEntity.ok("Welcome, USER!");
    }
}
