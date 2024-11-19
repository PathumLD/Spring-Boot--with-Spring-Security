package com.example.SpringBoot.with.JWT.service;

import com.example.SpringBoot.with.JWT.dto.AuthResponseDTO;
import com.example.SpringBoot.with.JWT.dto.LoginRequestDTO;
import com.example.SpringBoot.with.JWT.dto.RefreshTokenResponseDTO;
import com.example.SpringBoot.with.JWT.dto.RegisterRequestDTO;
import com.example.SpringBoot.with.JWT.entity.ERole;
import com.example.SpringBoot.with.JWT.entity.RefreshToken;
import com.example.SpringBoot.with.JWT.entity.Role;
import com.example.SpringBoot.with.JWT.entity.User;
import com.example.SpringBoot.with.JWT.repository.RoleRepository;
import com.example.SpringBoot.with.JWT.repository.UserRepository;
import com.example.SpringBoot.with.JWT.security.PasswordEncorder;
import com.example.SpringBoot.with.JWT.service.impl.UserDetailsImpl;
import com.example.SpringBoot.with.JWT.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncorder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    // Register a new user or admin

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
        // Check if username exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());

        // Set User role
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        // Add Admin role if registering an admin
        if (registerRequest.isAdmin()) {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Admin Role not found."));
            roles.add(adminRole);
        }

        user.setRoles(roles);
        userRepository.save(user);

        return login(LoginRequestDTO.builder()
                .email(registerRequest.getEmail())  //.usernameOrEmail(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build());
    }

    // Login user

    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthResponseDTO.builder()
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    // Refresh token

    public RefreshTokenResponseDTO refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtil.generateTokenFromUsername(user.getUsername());
                    return new RefreshTokenResponseDTO(token, refreshToken, "Bearer");
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    // Get admin role

    public Role getAdminRole() {
        return roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Admin Role not found. Please ensure database is properly initialized."));
    }

    // Check if admin exists

    public boolean existsAdmin() {
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Admin Role not found."));
        return userRepository.existsByRolesContaining(adminRole);
    }




    //username or email login

//    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getUsernameOrEmail(),
//                        loginRequest.getPassword())
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtUtil.generateToken(authentication);
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        return AuthResponseDTO.builder()
//                .token(jwt)
//                .id(userDetails.getId())
//                .username(userDetails.getUsername())
//                .email(userDetails.getEmail())
//                .roles(roles)
//                .build();
//    }
}
