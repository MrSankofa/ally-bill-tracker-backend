package com.altruistic_software_development.ally_bill_tracker_backend;

import com.altruistic_software_development.ally_bill_tracker_backend.config.ApiPaths;
import com.altruistic_software_development.ally_bill_tracker_backend.model.Role;
import com.altruistic_software_development.ally_bill_tracker_backend.model.User;
import com.altruistic_software_development.ally_bill_tracker_backend.repository.UserRepository;
import com.altruistic_software_development.ally_bill_tracker_backend.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Set;

/*
* TODO:
*
* add tests
*
Add negative tests (e.g. no token or invalid token)

Create protected endpoints that require specific roles (e.g. ADMIN)

Return authenticated user info from the token in your /protected-route response for debugging or verification
* */

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = User.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("password"))
                .roles(Set.of(Role.USER))
                .build();
        userRepository.save(user);
    }

    @Test
    void shouldAuthorizeRequestWithValidToken() throws Exception {
        User user = userRepository.findByEmail("test@example.com").get();
        String token = jwtService.generateToken(user.getEmail(), user.getRoles());

        mockMvc.perform(
                get(ApiPaths.BILLS_API + "/protected-route")
                    .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }
}
