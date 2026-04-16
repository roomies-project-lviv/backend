package com.roomies.backend.repositories;

import com.roomies.backend.models.RoommateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
                    // Порівняння булевих прапорців (кастимо значення з JSONB до boolean)
                    "(CASE WHEN CAST(u.lifestyle_flags->>'isSmoker' AS boolean) = :isSmoker THEN 10 ELSE 0 END + " +
                    " CASE WHEN CAST(u.lifestyle_flags->>'drinksAlcohol' AS boolean) = :drinksAlcohol THEN 10 ELSE 0 END + " +
                    " CASE WHEN CAST(u.lifestyle_flags->>'partyHabits' AS boolean) = :partyHabits THEN 10 ELSE 0 END + " +
                    // Порівняння рядкових значень
                    " CASE WHEN u.lifestyle_flags->>'sleepSchedule' = :sleep THEN 20 ELSE 0 END + " +
                    " CASE WHEN u.lifestyle_flags->>'cleanlinessLevel' = :clean THEN 20 ELSE 0 END + " +
                    " CASE WHEN u.lifestyle_flags->>'noiseTolerance' = :noise THEN 15 ELSE 0 END + " +
                    " CASE WHEN u.lifestyle_flags->>'guestsFrequency' = :guests THEN 10 ELSE 0 END + " +
                    " CASE WHEN u.lifestyle_flags->>'dietaryPreferences' = :diet THEN 5 ELSE 0 END) AS matchPercentage " +
                    "FROM roommate_requests r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.is_active = true " +
                    "AND r.target_city_id = :cityId " +
                    "AND u.id != cast(cast(:searcherId AS varchar) AS uuid) " +
                    "ORDER BY matchPercentage DESC",
            countQuery = "SELECT count(*) FROM roommate_requests r JOIN users u ON r.user_id = u.id WHERE r.is_active = true AND r.target_city_id = :cityId AND u.id != cast(cast(:searcherId AS varchar) AS uuid)",
            nativeQuery = true)
    Page<RoommateMatchProjection> findPotentialMatches(
            @Param("cityId") Long cityId,
            @Param("searcherId") UUID searcherId,
            @Param("isSmoker") Boolean isSmoker,
            @Param("drinksAlcohol") Boolean drinksAlcohol,
            @Param("partyHabits") Boolean partyHabits,
            @Param("sleep") String sleep,
            @Param("clean") String clean,
            @Param("noise") String noise,
            @Param("guests") String guests,
            @Param("diet") String diet,
            Pageable pageable);
}