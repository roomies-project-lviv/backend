package com.roomies.backend.controllers;

import com.roomies.backend.dto.RoommateRequestDto;
import com.roomies.backend.services.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/potential-matches")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    // Приклад запиту: GET /api/users/123-uuid/potential-matches?cityId=1&size=10
    @GetMapping
    public ResponseEntity<Page<RoommateRequestDto>> getMatches(
            @PathVariable UUID userId,
            @RequestParam Long cityId, // Місто обов'язкове як базовий фільтр
            @PageableDefault(size = 20) Pageable pageable) { // Дефолтний ліміт 20 записів

        Page<RoommateRequestDto> matches = matchingService.getPotentialMatches(userId, cityId, pageable);
        return ResponseEntity.ok(matches);
    }
}