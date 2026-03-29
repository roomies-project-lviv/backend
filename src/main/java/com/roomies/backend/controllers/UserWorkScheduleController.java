package com.roomies.backend.controllers;

import com.roomies.backend.dto.UserWorkScheduleDto;
import com.roomies.backend.services.UserWorkScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/work-schedule")
public class UserWorkScheduleController {

    @Autowired
    private UserWorkScheduleService scheduleService;

    // GET /api/users/{userId}/work-schedule
    @GetMapping
    public ResponseEntity<?> getWorkSchedule(@PathVariable UUID userId) {
        UserWorkScheduleDto schedule = scheduleService.getSchedule(userId);

        // Якщо розкладу немає
        if (schedule == null) {
            // Повертаємо статус 404 (Не знайдено) і гарний JSON з повідомленням
            return ResponseEntity.status(404)
                    .body(java.util.Map.of("message", "Розклад для цього користувача ще не створено"));
        }

        // Якщо розклад є - повертаємо його зі статусом 200 (ОК)
        return ResponseEntity.ok(schedule);
    }

    // PUT /api/users/{userId}/work-schedule (Створює новий або оновлює існуючий)
    @PutMapping
    public ResponseEntity<UserWorkScheduleDto> updateWorkSchedule(
            @PathVariable UUID userId,
            @RequestBody UserWorkScheduleDto scheduleDto) {

        return ResponseEntity.ok(scheduleService.updateOrCreateSchedule(userId, scheduleDto));
    }

    // DELETE /api/users/{userId}/work-schedule
    @DeleteMapping
    public ResponseEntity<Void> deleteWorkSchedule(@PathVariable UUID userId) {
        scheduleService.deleteSchedule(userId);
        return ResponseEntity.noContent().build();
    }
}