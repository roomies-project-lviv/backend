package com.roomies.backend.controllers;

import com.roomies.backend.dto.FavoriteRoommateRequestCreateDto;
import com.roomies.backend.dto.FavoriteRoommateRequestDto;
import com.roomies.backend.security.SecurityUtils;
import com.roomies.backend.services.FavoriteRoommateRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites/roommate-requests")
public class FavoriteRoommateRequestController {

    @Autowired
    private FavoriteRoommateRequestService favoriteService;
    @Autowired private SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<FavoriteRoommateRequestDto> addFavorite(@RequestBody FavoriteRoommateRequestCreateDto actionDto) {
        FavoriteRoommateRequestDto result = favoriteService.addFavorite(securityUtils.getCurrentUser().getId(), actionDto.getRequestId());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestBody FavoriteRoommateRequestCreateDto actionDto) {
        favoriteService.removeFavorite(securityUtils.getCurrentUser().getId(), actionDto.getRequestId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteRoommateRequestDto>> getUserFavorites(@PathVariable UUID userId) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(userId));
    }
}