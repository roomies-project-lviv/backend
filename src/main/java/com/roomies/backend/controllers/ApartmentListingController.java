package com.roomies.backend.controllers;

import com.roomies.backend.dto.ApartmentListingCreateDto;
import com.roomies.backend.dto.ApartmentListingDto;
import com.roomies.backend.dto.filters.ListingFilterDto;
import com.roomies.backend.services.ApartmentListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/listings")
public class ApartmentListingController {

    @Autowired
    private ApartmentListingService listingService;

    @Autowired
    private SupabaseStorageService storageService;

    @GetMapping
    public ResponseEntity<Page<ApartmentListingDto>> getAllActiveListings(Pageable pageable) {
        return ResponseEntity.ok(listingService.getAllActiveListings(pageable));
    }

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

   @PostMapping("/{id}/image")
   public ResponseEntity<ApartmentListingDto> uploadListingImage(
        @PathVariable UUID id,
        @RequestParam("file") MultipartFile file) throws IOException 
        {
    
    ApartmentListingDto listing = listingsService.getListingById(id);
    
    String fileName = "listing-" + id + "-" + System.currentTimeMillis();
    String publicUrl = storageService.uploadFile(file, "apartments", fileName);
    
    ApartmentListingCreateDto updateDto = new ApartmentListingCreateDto();
    updateDto.setTitle(listing.getTitle());
    updateDto.setPricePerMonth(listing.getPricePerMonth());
    updateDto.setImageUrl(publicUrl); // Ось тут ми зберігаємо посилання
    
    return ResponseEntity.ok(listingsService.updateListing(id, updateDto));
}
    
}