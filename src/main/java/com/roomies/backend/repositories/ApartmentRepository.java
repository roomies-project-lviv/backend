package com.roomies.backend.repositories;

import com.roomies.backend.models.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, UUID> {
    // Поки що нам достатньо стандартних методів JpaRepository
}