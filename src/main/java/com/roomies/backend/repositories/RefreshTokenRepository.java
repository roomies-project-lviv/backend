package com.roomies.backend.repositories;

import com.roomies.backend.models.RefreshToken;
import com.roomies.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user); // Щоб видаляти старий токен при новому логіні
    
}