package com.altruistic_software_development.ally_bill_tracker_backend.dto;

import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String userId;
    private String email;
    private Set<Role> roles;
    private String token;
}
