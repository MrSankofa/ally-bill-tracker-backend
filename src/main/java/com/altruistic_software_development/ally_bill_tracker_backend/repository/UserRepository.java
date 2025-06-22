package com.altruistic_software_development.ally_bill_tracker_backend.repository;


import com.altruistic_software_development.ally_bill_tracker_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
