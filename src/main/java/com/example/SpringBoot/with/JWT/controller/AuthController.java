package com.example.SpringBoot.with.JWT.controller;

import com.example.SpringBoot.with.JWT.dto.AuthResponseDTO;
import com.example.SpringBoot.with.JWT.dto.LoginRequestDTO;
import com.example.SpringBoot.with.JWT.dto.RegisterRequestDTO;
import com.example.SpringBoot.with.JWT.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO registerRequest
    ) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequest
    ) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}