package com.example.SpringBoot.with.JWT.repository;

import com.example.SpringBoot.with.JWT.entity.ERole;
import com.example.SpringBoot.with.JWT.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Find role by name
    Optional<Role> findByName(ERole name);

}
