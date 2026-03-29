package com.roomies.backend.controllers;

import com.roomies.backend.dto.ApartmentListingCreateDto;
import com.roomies.backend.dto.ApartmentListingDto;
import com.roomies.backend.services.ApartmentListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/listings")
public class ApartmentListingController {

    @Autowired
    private ApartmentListingService listingService;

    @GetMapping
    public ResponseEntity<List<ApartmentListingDto>> getAllActiveListings() {
        return ResponseEntity.ok(listingService.getAllActiveListings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentListingDto> getListingById(@PathVariable UUID id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @PostMapping
    public ResponseEntity<ApartmentListingDto> createListing(@RequestBody ApartmentListingCreateDto createDto) {
        return new ResponseEntity<>(listingService.createListing(createDto), HttpStatus.CREATED);
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

    
}