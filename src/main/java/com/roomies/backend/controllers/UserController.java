package com.roomies.backend.controllers;

import com.roomies.backend.security.SecurityUtils;
import com.roomies.backend.dto.UserCreateDto;
import com.roomies.backend.dto.UserDto;
import com.roomies.backend.models.User;
import com.roomies.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/users")
public class UserController {

    @Autowired 
    private UserService userService;

    @Autowired
    private SecurityUtils securityUtils;

    // Отримати дані про себе
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    // Оновити свій профіль
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMyProfile(@RequestBody UserDto userDetails) {
        return ResponseEntity.ok(userService.updateMyProfile(userDetails));
    }

    // Оновити свої преференції (прапорці тощо)
    @PatchMapping("/me/preferences")
    public ResponseEntity<UserDto> updateMyPreferences(@RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(userService.updateMyPreferences(updates));
    }

    @PostMapping(value = "/me/avatar", consumes = "multipart/form-data")
    public ResponseEntity<UserDto> uploadAvatar(
            @RequestParam("file") MultipartFile file) { // Прибираємо Authentication звідси
        try {
            // Отримуємо поточного юзера через SecurityUtils, як ти робиш в інших методах
            User currentUser = securityUtils.getCurrentUser();
            UUID userId = currentUser.getId(); 

            UserDto updatedUser = userService.uploadUserAvatar(userId, file);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            e.printStackTrace(); // Щоб бачити помилку в терміналі, якщо щось з Supabase
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
}