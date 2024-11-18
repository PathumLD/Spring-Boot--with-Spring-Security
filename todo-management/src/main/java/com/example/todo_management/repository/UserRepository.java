package com.example.todo_management.repository;

import com.example.todo_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    //Boolean existByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);
}
