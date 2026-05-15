package com.roomies.backend.repositories;

import com.roomies.backend.models.ApartmentListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ApartmentListingRepository extends JpaRepository<ApartmentListing, UUID>, JpaSpecificationExecutor<ApartmentListing> {
    List<ApartmentListing> findByIsActiveTrue();
    List<ApartmentListing> findByAuthorId(UUID authorId);
    Page<ApartmentListing> findByAuthorId(UUID authorId, Pageable pageable);
    Page<ApartmentListing> findByIsActiveTrue(Pageable pageable); // Замінили List на Page

}