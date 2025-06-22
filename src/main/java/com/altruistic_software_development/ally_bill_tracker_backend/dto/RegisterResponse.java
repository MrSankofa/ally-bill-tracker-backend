package com.altruistic_software_development.ally_bill_tracker_backend.dto;

import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter @Builder
public class RegisterResponse {
    private String userId;
    private String email;
    private Set<Role> roles;
}
