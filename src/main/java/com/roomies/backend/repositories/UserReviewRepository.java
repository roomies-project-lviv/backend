package com.roomies.backend.repositories;

import com.roomies.backend.models.UserReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, UUID> {

    // ОНОВЛЕНО: Тепер використовуємо Page та Pageable
    Page<UserReview> findByTargetUserIdOrderByCreatedAtDesc(UUID targetUserId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM UserReview r WHERE r.targetUser.id = :targetUserId")
    Double getAverageRatingForUser(@Param("targetUserId") UUID targetUserId);

    boolean existsByAuthorIdAndTargetUserId(UUID authorId, UUID targetUserId);
}