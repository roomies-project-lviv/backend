package com.roomies.backend.repositories;

import com.roomies.backend.models.FavoriteApartmentListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteApartmentListingRepository extends JpaRepository<FavoriteApartmentListing, UUID> {
    List<FavoriteApartmentListing> findByUserId(UUID userId);
    boolean existsByUserIdAndListingId(UUID userId, UUID listingId);
    Optional<FavoriteApartmentListing> findByUserIdAndListingId(UUID userId, UUID listingId);
    
}