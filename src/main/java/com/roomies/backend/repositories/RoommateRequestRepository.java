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


    @Query("SELECT r AS request, " +
            // ДОДАНО cast(...) as string для lifestyleFlags
            "(CASE WHEN cast(r.author.lifestyleFlags as string) = :lifestyle THEN 30 ELSE 0 END + " +
            " CASE WHEN r.author.sleepSchedule = :sleep THEN 20 ELSE 0 END + " +
            " CASE WHEN r.author.cleanlinessLevel = :clean THEN 20 ELSE 0 END + " +
            " CASE WHEN r.author.noiseTolerance = :noise THEN 15 ELSE 0 END + " +
            " CASE WHEN r.author.guestsFrequency = :guests THEN 10 ELSE 0 END + " +
            " CASE WHEN r.author.dietaryPreferences = :diet THEN 5 ELSE 0 END) AS matchPercentage " +
            "FROM RoommateRequest r " +
            "WHERE r.isActive = true " +
            "AND r.targetCity.id = :cityId " +
            "AND r.author.id != :searcherId " +
            "ORDER BY " +
            // ТУТ ТАКОЖ ДОДАНО cast(...) as string
            "(CASE WHEN cast(r.author.lifestyleFlags as string) = :lifestyle THEN 30 ELSE 0 END + " +
            " CASE WHEN r.author.sleepSchedule = :sleep THEN 20 ELSE 0 END + " +
            " CASE WHEN r.author.cleanlinessLevel = :clean THEN 20 ELSE 0 END + " +
            " CASE WHEN r.author.noiseTolerance = :noise THEN 15 ELSE 0 END + " +
            " CASE WHEN r.author.guestsFrequency = :guests THEN 10 ELSE 0 END + " +
            " CASE WHEN r.author.dietaryPreferences = :diet THEN 5 ELSE 0 END) DESC")
    Page<RoommateMatchProjection> findPotentialMatches(
            @Param("cityId") Long cityId,
            @Param("searcherId") UUID searcherId,
            @Param("lifestyle") String lifestyle,
            @Param("sleep") String sleep,
            @Param("clean") String clean,
            @Param("noise") String noise,
            @Param("guests") String guests,
            @Param("diet") String diet,
            Pageable pageable);
}