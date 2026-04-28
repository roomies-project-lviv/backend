package com.roomies.backend.controllers;

import com.roomies.backend.dto.UserReviewCreateDto;
import com.roomies.backend.dto.UserReviewDto;
import com.roomies.backend.dto.UserReviewUpdateDto;
import com.roomies.backend.services.UserReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class UserReviewController {

    @Autowired
    private UserReviewService reviewService;

    // ВИПРАВЛЕНО: Тепер це базовий GET /api/reviews із параметром фільтрації
    @GetMapping
    public ResponseEntity<Page<UserReviewDto>> getReviewsForUser(
            @RequestParam UUID targetUserId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewsForUser(targetUserId, pageable));
    }


    // ВИПРАВЛЕНО: Тепер це GET /api/reviews/average із параметром
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageRating(@RequestParam UUID targetUserId) {
        return ResponseEntity.ok(reviewService.getUserAverageRating(targetUserId));
    }

    @PostMapping
    public ResponseEntity<UserReviewDto> createReview(@Valid @RequestBody UserReviewCreateDto createDto) {
        return new ResponseEntity<>(reviewService.createReview(createDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserReviewDto> updateReview(
            @PathVariable UUID id,
            @Valid @RequestBody UserReviewUpdateDto updateDto) {
        return ResponseEntity.ok(reviewService.updateReview(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Page<UserReviewDto>> getMyReviews(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getMyReviews(pageable));
    }

    @GetMapping("/me/average")
    public ResponseEntity<Double> getMyAverageRating() {
        return ResponseEntity.ok(reviewService.getMyAverageRating());
    }
}