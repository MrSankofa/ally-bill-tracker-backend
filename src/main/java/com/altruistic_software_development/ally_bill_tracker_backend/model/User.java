package com.altruistic_software_development.ally_bill_tracker_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    /*@ElementCollection is used when you want to store a collection of simple values
    (like String, Integer, or Enum) that belong to an entity but aren’t separate entities themselves
    I want this user to have a set of roles (e.g., USER, ADMIN),
    and I want those roles stored in a simple side table — not as a separate entity.

     Always load the roles with the user (good for security contexts)

     Lazy vs. Eager Loading

- Lazy Loading: Default in JPA; fetches related entities only when accessed.
This minimizes the initial database call but can cause the N+1 query problem if poorly managed.
- Eager Loading: Fetches related entities immediately.
While it reduces the number of queries, it can increase the amount of data loaded unnecessarily,
leading to slower queries and higher memory usage.
    */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
