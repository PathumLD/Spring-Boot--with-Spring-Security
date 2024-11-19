package com.example.SpringBoot.with.JWT.controller;

import com.example.SpringBoot.with.JWT.dto.*;
import com.example.SpringBoot.with.JWT.service.AuthService;
import com.example.SpringBoot.with.JWT.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

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

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        try {
            return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponseDTO(
                            java.time.LocalDateTime.now(),
                            400,
                            "Error",
                            e.getMessage(),
                            "/api/v1/auth/refresh-token"
                    ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestParam Long userId) {
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(Map.of("message", "Log out successful!"));
    }
}
