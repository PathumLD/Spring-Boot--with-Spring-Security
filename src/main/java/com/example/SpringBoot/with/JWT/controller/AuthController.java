package com.example.SpringBoot.with.JWT.controller;

import com.example.SpringBoot.with.JWT.dto.AuthResponseDTO;
import com.example.SpringBoot.with.JWT.dto.LoginRequestDTO;
import com.example.SpringBoot.with.JWT.dto.RegisterRequestDTO;
import com.example.SpringBoot.with.JWT.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO registerRequest
    ) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    // Login a user

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequest
    ) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    // Register a new admin

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')") // Only existing admins can create new admins
    public ResponseEntity<AuthResponseDTO> registerAdmin(
            @Valid @RequestBody RegisterRequestDTO registerRequest
    ) {
        // Set the admin flag to true for this registration
        registerRequest.setAdmin(true);
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    // Register the first admin

    @PostMapping("/register/first-admin")
    public ResponseEntity<?> registerFirstAdmin(
            @Valid @RequestBody RegisterRequestDTO registerRequest
    ) {
        // Check if any admin exists
        if (authService.existsAdmin()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Admin already exists"));
        }
        registerRequest.setAdmin(true);
        return ResponseEntity.ok(authService.register(registerRequest));
    }
}
