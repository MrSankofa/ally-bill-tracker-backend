package com.altruistic_software_development.ally_bill_tracker_backend.dto;

import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;
    private Set<Role> roles;
}
