package com.roomies.backend.controllers;

import com.roomies.backend.dto.UserReviewCreateDto;
import com.roomies.backend.dto.UserReviewDto;
import com.roomies.backend.services.UserReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class UserReviewController {

    @Autowired
    private UserReviewService reviewService;

    // Отримати всі відгуки на користувача
    @GetMapping("/user/{targetUserId}")
    public ResponseEntity<List<UserReviewDto>> getReviewsForUser(@PathVariable UUID targetUserId) {
        return ResponseEntity.ok(reviewService.getReviewsForUser(targetUserId));
    }

    // Отримати середній рейтинг користувача
    @GetMapping("/user/{targetUserId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable UUID targetUserId) {
        return ResponseEntity.ok(reviewService.getUserAverageRating(targetUserId));
    }

    // Створити новий відгук
    @PostMapping
    public ResponseEntity<UserReviewDto> createReview(@RequestBody UserReviewCreateDto createDto) {
        return new ResponseEntity<>(reviewService.createReview(createDto), HttpStatus.CREATED);
    }

    // Видалити відгук
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    
}