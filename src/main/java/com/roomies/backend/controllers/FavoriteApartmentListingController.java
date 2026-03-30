package com.roomies.backend.controllers;

import com.roomies.backend.dto.FavoriteListingRequestDto;
import com.roomies.backend.services.FavoriteApartmentListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites/listings")
public class FavoriteApartmentListingController {

    @Autowired
    private FavoriteApartmentListingService favoriteService;

    @PostMapping
    public ResponseEntity<String> addFavorite(@RequestBody FavoriteListingRequestDto request) {
        favoriteService.addFavorite(request);
        return ResponseEntity.ok("Додано до обраного");
    }

    @DeleteMapping
    public ResponseEntity<String> removeFavorite(@RequestBody FavoriteListingRequestDto request) {
        favoriteService.removeFavorite(request);
        return ResponseEntity.ok("Видалено з обраного");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UUID>> getUserFavorites(@PathVariable UUID userId) {
        return ResponseEntity.ok(favoriteService.getUserFavoriteListingIds(userId));
    }

    
}