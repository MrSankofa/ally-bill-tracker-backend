package com.altruistic_software_development.ally_bill_tracker_backend.dto;

import lombok.Getter;
import lombok.Setter;

// ===============================================
// Purpose: This class handles user login requests
// Type: DTO (Data Transfer Object)
// Structure: Receives email + password from client
// Security Note: We never expose passwords in return
// ===============================================

/*
*  Why this is important:
*  - decouples frontend input from internal models.
*  * You never expose internal User entity directly - that's bad practice. DTOs are the
*  * "boundary objects" for API communication.
*
* Boundary objects are data structures that sit at the edges (boundaries) of your application — especially between:

External systems (like your frontend or other services) and

Your internal domain logic (like entities, services, and DB models)

They serve as a safe, controlled way to receive and send data across these boundaries.
*
* [ Client (React) ]
      ↓
[ Controllers (Boundary Layer) ]
      ↓
[ Services (Business Logic) ]
      ↓
[ Repositories (Data Layer) ]

* DTOs live at the boundary layer — right between the client and the controller.

They are used to receive incoming requests and format outgoing responses.
*
*
* - validates and sanitizes input
* * You can attach validations rules to DTOs later (@Email, @Size(min=)
*
* - prevents over-posting security risks
* * If you exposed your full User entity, a malicious user could set fields they shouldn't
* * isAdmin=true for example
*
* - Keeps your domain model clean
* * DTOs act like input contracts - only fields that should be writable are exposed
*
*  - you can change your domain logic without breaking API contracts if they're shielded by DTOS
*
* end to end auth flow
*
* front end sends a JSON body like
*
{
  "email": "bwc@example.com",
  "password": "StrongPassword123"
}

* spring maps this to RegisterREquest
* We use that DTO to create a new User with the jpa, hash the password, and save it to the DB
* you don't expose the actual User object outside word.
*
Client (React) ⟶ [ JSON ] ⟶
  AuthController (@RestController)
      ⬇
  DTOs (RegisterRequest, AuthRequest)
      ⬇
  Business logic (UserRepository, PasswordEncoder, JWTService)
      ⬇
  Response (String / Token)

*
*
Why Use Boundary Objects (DTOs) Instead of Entities?
Concern	        If You Used Entity Directly	                      If You Used DTO (Boundary Object)
Security	    Risk of over-posting (e.g., setting admin role)	     Only exposed fields are accepted
Validation	    Harder to validate entity input safely	             You can annotate DTOs with @Valid rules
Encapsulation	Client knows your entire DB structure	             Client sees only what it needs
Flexibility	    Changes in your entity can break clients	         DTOs act as a stable contract
Layer Separation Domain and API are tightly coupled	                 Clean boundaries between layers
*
Analogy
Think of a boundary object like the check-in desk at an airport:

You show only the necessary documents (e.g., ID, boarding pass)

The staff doesn’t see your entire suitcase (your full entity)

The check-in process stays safe, fast, and controlled

* */

@Getter
@Setter
public class AuthRequest {
    // TODO: validation rules for email and password
    private String email;
    private String password;
}
