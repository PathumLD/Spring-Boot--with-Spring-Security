package com.example.SpringBoot.with.JWT.repository;

import com.example.SpringBoot.with.JWT.entity.Role;
import com.example.SpringBoot.with.JWT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username
    Optional<User> findByUsername(String username);

    // Add method to find user by email
    Optional<User> findByEmail(String email);

    // Check if user has a role
    boolean existsByRolesContaining(Role role);

    // Find user by username or email
//    Optional<User> findByUsernameOrEmail(String username, String email);

    // Check if username exists
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);
}
