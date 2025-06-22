package com.altruistic_software_development.ally_bill_tracker_backend.controller;

import com.altruistic_software_development.ally_bill_tracker_backend.config.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.BILLS_API)
public class BillController {

    @GetMapping("/protected-route")
    public ResponseEntity<?> getProtectedRoute() {
        return ResponseEntity.ok().build();
    }
}
