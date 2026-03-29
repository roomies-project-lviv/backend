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
    public ResponseEntity<UserWorkScheduleDto> getWorkSchedule(@PathVariable UUID userId) {
        return ResponseEntity.ok(scheduleService.getSchedule(userId));
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