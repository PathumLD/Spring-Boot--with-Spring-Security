package com.example.SpringBoot.with.JWT.config;

import com.example.SpringBoot.with.JWT.entity.ERole;
import com.example.SpringBoot.with.JWT.entity.Role;
import com.example.SpringBoot.with.JWT.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializationConfig {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        createRoleIfNotExists(ERole.ROLE_ADMIN);
        createRoleIfNotExists(ERole.ROLE_USER);
    }

    private void createRoleIfNotExists(ERole roleName) {
        if (!roleRepository.findByName(roleName).isPresent()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
