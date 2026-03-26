package com.roomies.backend.repositories;

import com.roomies.backend.models.UserSocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserSocialLinkRepository extends JpaRepository<UserSocialLink, UUID> {
    
    // Кастомний метод: Знайти всі соціальні мережі конкретного користувача
    List<UserSocialLink> findByUserId(UUID userId);
    
}