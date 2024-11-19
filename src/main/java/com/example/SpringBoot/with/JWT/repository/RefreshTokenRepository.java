package com.example.SpringBoot.with.JWT.repository;

import com.example.SpringBoot.with.JWT.entity.RefreshToken;
import com.example.SpringBoot.with.JWT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);
}