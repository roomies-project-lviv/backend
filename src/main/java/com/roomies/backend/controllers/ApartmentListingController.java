package com.roomies.backend.controllers;

import com.roomies.backend.dto.ApartmentListingCreateDto;
import com.roomies.backend.dto.ApartmentListingDto;
import com.roomies.backend.dto.filters.ListingFilterDto;
import com.roomies.backend.models.User;
import com.roomies.backend.services.ApartmentListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.roomies.backend.security.SecurityUtils;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/listings")
public class ApartmentListingController {

    @Autowired
    private ApartmentListingService listingService;
    @Autowired
    private SecurityUtils securityUtils;

    // Замінюємо існуючий GET метод на POST для передачі фільтрів у тілі запиту
    @PostMapping("/search")
    public ResponseEntity<Page<ApartmentListingDto>> searchListings(
            @RequestBody(required = false) ListingFilterDto filterDto, 
            Pageable pageable) {
        
        if (filterDto == null) filterDto = new ListingFilterDto(); // Якщо без фільтрів
        return ResponseEntity.ok(listingService.getAllActiveListings(filterDto, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentListingDto> getListingById(@PathVariable UUID id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApartmentListingDto> createListing(
            @RequestPart("data") ApartmentListingCreateDto createDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        // Отримуємо користувача безпосередньо з SecurityUtils
        User currentUser = securityUtils.getCurrentUser();

        return new ResponseEntity<>(listingService.createListing(createDto, currentUser, images), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable UUID id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApartmentListingDto> updateListing(@PathVariable UUID id, @RequestBody ApartmentListingCreateDto updateDto) {
        return ResponseEntity.ok(listingService.updateListing(id, updateDto));
    }

    @GetMapping
    public ResponseEntity<Page<ApartmentListingDto>> getAllListings(
            @ModelAttribute ListingFilterDto filter, // Збирає всі параметри з URL
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 20) Pageable pageable) {

        Page<ApartmentListingDto> listings = listingService.getFilteredListings(filter, pageable);
        return ResponseEntity.ok(listings);
    }
    
}