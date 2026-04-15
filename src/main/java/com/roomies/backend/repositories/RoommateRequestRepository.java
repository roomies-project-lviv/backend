package com.roomies.backend.repositories;

import com.roomies.backend.models.RoommateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoommateRequestRepository extends JpaRepository<RoommateRequest, UUID>, JpaSpecificationExecutor<RoommateRequest> {
    // Метод для відображення лише активних анкет у стрічці
    List<RoommateRequest> findByIsActiveTrue();

    // Метод для пошуку анкет конкретного користувача
    List<RoommateRequest> findByAuthorId(UUID authorId);


    @Query(value =
            "SELECT r.id AS requestId, " +
                    // Використовуємо оператор @> для перевірки, чи містить JSON кандидата ті ж ключі/значення, що і JSON шукача
                    "(CASE WHEN u.lifestyle_flags @> cast(:lifestyleJson as jsonb) THEN 30 ELSE 0 END + " +
                    " CASE WHEN u.sleep_schedule = :sleep THEN 20 ELSE 0 END + " +
                    " CASE WHEN u.cleanliness_level = :clean THEN 20 ELSE 0 END + " +
                    " CASE WHEN u.noise_tolerance = :noise THEN 15 ELSE 0 END + " +
                    " CASE WHEN u.guests_frequency = :guests THEN 10 ELSE 0 END + " +
                    " CASE WHEN u.dietary_preferences = :diet THEN 5 ELSE 0 END) AS matchPercentage " +
                    "FROM roommate_requests r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.is_active = true " +
                    "AND r.target_city_id = :cityId " +
                    "AND u.id != cast(cast(:searcherId AS varchar) AS uuid) " +
                    "ORDER BY matchPercentage DESC",
            // Оскільки це Native Query з пагінацією, Spring Data потребує countQuery
            countQuery = "SELECT count(*) FROM roommate_requests r JOIN users u ON r.user_id = u.id WHERE r.is_active = true AND r.target_city_id = :cityId AND u.id != cast(cast(:searcherId AS varchar) AS uuid)",
            nativeQuery = true)
    Page<RoommateMatchProjection> findPotentialMatches(
            @Param("cityId") Long cityId,
            @Param("searcherId") UUID searcherId,
            @Param("lifestyleJson") String lifestyleJson, // Передаємо JSON як звичайний рядок
            @Param("sleep") String sleep,
            @Param("clean") String clean,
            @Param("noise") String noise,
            @Param("guests") String guests,
            @Param("diet") String diet,
            Pageable pageable);
}