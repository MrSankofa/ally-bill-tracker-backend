package com.altruistic_software_development.ally_bill_tracker_backend.controller;

import com.altruistic_software_development.ally_bill_tracker_backend.config.ApiPaths;
import com.altruistic_software_development.ally_bill_tracker_backend.dto.AuthRequest;
import com.altruistic_software_development.ally_bill_tracker_backend.dto.LoginResponse;
import com.altruistic_software_development.ally_bill_tracker_backend.dto.RegisterRequest;
import com.altruistic_software_development.ally_bill_tracker_backend.dto.RegisterResponse;
import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import com.altruistic_software_development.ally_bill_tracker_backend.model.User;
import com.altruistic_software_development.ally_bill_tracker_backend.repository.UserRepository;
import com.altruistic_software_development.ally_bill_tracker_backend.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(ApiPaths.AUTH_API)
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        // check if user already exists

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    RegisterResponse.builder()
                            .email(request.getEmail())
                            .roles(request.getRoles())
                            .message("User with email already exists")
                            .build()
            );
        }


        Set<Role> roles = Optional.ofNullable(request.getRoles()).orElse(Set.of(Role.USER));

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .roles(roles)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRoles());

        RegisterResponse registerResponse = RegisterResponse.builder()
                .email(request.getEmail())
                .userId(user.getId())
                .roles(user.getRoles())
                .token(token)
                .build();

        URI location = URI.create("/api/users/" + user.getId());
        return ResponseEntity.created(location).body(registerResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found - Invalid email"));

        if(!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }


        String token = jwtService.generateToken(user.getEmail(), user.getRoles());

        LoginResponse loginResponse = LoginResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles())
                .token(token)
                .build();

        return ResponseEntity.ok(loginResponse);
    }


}
