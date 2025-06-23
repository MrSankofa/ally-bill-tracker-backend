package com.altruistic_software_development.ally_bill_tracker_backend.dto;

import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import com.altruistic_software_development.ally_bill_tracker_backend.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    private Set<Role> roles;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(Set.of(Role.USER))  // Enforces least-privilege
                .build();
    }
}
