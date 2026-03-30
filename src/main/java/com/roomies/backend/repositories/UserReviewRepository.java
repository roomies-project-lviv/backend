package com.roomies.backend.repositories;

import com.roomies.backend.models.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, UUID> {
    
    // Отримати всі відгуки на конкретного користувача (відсортовані від нових до старих)
    List<UserReview> findByTargetUserIdOrderByCreatedAtDesc(UUID targetUserId);
    
    // Перевірка, чи вже залишав користувач відгук
    boolean existsByAuthorIdAndTargetUserId(UUID authorId, UUID targetUserId);

    // Автоматичний підрахунок середнього рейтингу (повертає 0.0, якщо відгуків немає)
    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM UserReview r WHERE r.targetUser.id = :userId")
    Double getAverageRatingForUser(@Param("userId") UUID userId);

    
}