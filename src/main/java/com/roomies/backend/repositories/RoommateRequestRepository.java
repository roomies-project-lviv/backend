package com.roomies.backend.repositories;

import com.roomies.backend.models.RoommateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoommateRequestRepository extends JpaRepository<RoommateRequest, UUID> {
    // Метод для відображення лише активних анкет у стрічці
    List<RoommateRequest> findByIsActiveTrue();
    
    // Метод для пошуку анкет конкретного користувача
    List<RoommateRequest> findByAuthorId(UUID authorId);
    
}