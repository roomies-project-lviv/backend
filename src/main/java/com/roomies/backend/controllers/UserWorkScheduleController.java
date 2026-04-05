package com.roomies.backend.controllers;

import com.roomies.backend.dto.UserWorkScheduleDto;
import com.roomies.backend.services.UserWorkScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-schedule") // Чистий, безпечний шлях
public class UserWorkScheduleController {

    @Autowired private UserWorkScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<?> getMyWorkSchedule() {
        UserWorkScheduleDto schedule = scheduleService.getMySchedule();
        if (schedule == null) {
            return ResponseEntity.status(404)
                    .body(java.util.Map.of("message", "Розклад ще не створено"));
        }
        return ResponseEntity.ok(schedule);
    }

    @PutMapping
    public ResponseEntity<UserWorkScheduleDto> updateMyWorkSchedule(@RequestBody UserWorkScheduleDto scheduleDto) {
        return ResponseEntity.ok(scheduleService.updateOrCreateMySchedule(scheduleDto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMyWorkSchedule() {
        scheduleService.deleteMySchedule();
        return ResponseEntity.noContent().build();
    }

    
}