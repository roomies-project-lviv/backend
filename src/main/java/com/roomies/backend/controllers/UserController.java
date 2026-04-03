package com.roomies.backend.controllers;

import com.roomies.backend.dto.UserCreateDto;
import com.roomies.backend.dto.UserDto;
import com.roomies.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/users")
public class UserController {

    @Autowired 
    private UserService userService;

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

/*
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id)); 
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserDto createdUser = userService.createUser(userCreateDto); 
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserProfile(@PathVariable UUID id, @RequestBody UserDto userDetails) {
        return ResponseEntity.ok(userService.updateUserProfile(id, userDetails));
    }

    @PatchMapping("/{id}/preferences")
    public ResponseEntity<UserDto> updateUserPreferences(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(userService.updateUserPreferences(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); 
    }
*/
    
}