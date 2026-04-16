package com.roomies.backend.controllers;

import com.roomies.backend.dto.RoommateRequestDto;
import com.roomies.backend.security.SecurityUtils;
import com.roomies.backend.services.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
// ОНОВЛЕНО: Шлях тепер відповідає концепції /me
@RequestMapping("/api/users/me/potential-matches")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    // ДОДАНО: Інжектимо SecurityUtils для отримання поточного користувача
    @Autowired
    private SecurityUtils securityUtils;

    // Приклад запиту: GET /api/users/me/potential-matches?cityId=1&size=10
    @GetMapping
    public ResponseEntity<Page<RoommateRequestDto>> getMatches(
            @RequestParam Long cityId, // Місто обов'язкове як базовий фільтр
            @PageableDefault(size = 20) Pageable pageable) {

        // ОНОВЛЕНО: Беремо ID з токена авторизації, а не з URL
        UUID currentUserId = securityUtils.getCurrentUser().getId();

        Page<RoommateRequestDto> matches = matchingService.getPotentialMatches(currentUserId, cityId, pageable);
        return ResponseEntity.ok(matches);
    }
}