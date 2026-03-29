package com.roomies.backend.repositories;

import com.roomies.backend.models.ApartmentListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApartmentListingRepository extends JpaRepository<ApartmentListing, UUID> {
    List<ApartmentListing> findByIsActiveTrue();
    List<ApartmentListing> findByAuthorId(UUID authorId);
    
}