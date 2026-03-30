package com.roomies.backend.repositories;

import com.roomies.backend.models.FavoriteRoommateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteRoommateRequestRepository extends JpaRepository<FavoriteRoommateRequest, UUID> {

    // Знайти всі лайки конкретного користувача
    List<FavoriteRoommateRequest> findByUserId(UUID userId);

    // Перевірити, чи вже лайкнув користувач цей конкретний запит
    boolean existsByUserIdAndRequestId(UUID userId, UUID requestId);

    // Видалити лайк
    void deleteByUserIdAndRequestId(UUID userId, UUID requestId);
}