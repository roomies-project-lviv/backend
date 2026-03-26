package com.roomies.backend.controllers;

import com.roomies.backend.exceptions.UserNotFoundException;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController 
@RequestMapping("/api/users") // Це базова адреса. Всі запити сюди починатимуться з /api/users
public class UserController {

    @Autowired // Автоматично підключаємо наш репозиторій, щоб ми могли працювати з базою даних
    private UserRepository userRepository;

    // Цей метод спрацює, коли хтось перейде за адресою /api/users
    @GetMapping
    public List<User> getAllUsers() {
        // Просимо репозиторій знайти ВСІХ користувачів у базі і повертаємо їх
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        // GET /api/users/{id} - Отримати одного користувача за ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        return ResponseEntity.ok(user); // Повертаємо статус 200 OK і самого користувача
    }

    // POST /api/users - Створити нового користувача
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user); // Зберігаємо в базу
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // Повертаємо 201 Created
    }

    // PUT /api/users/{id} - Повністю оновити базові дані профілю
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserProfile(@PathVariable UUID id, @RequestBody User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        // Оновлюємо базові поля
        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setBirthDate(userDetails.getBirthDate());
        existingUser.setGender(userDetails.getGender());
        existingUser.setAvatarUrl(userDetails.getAvatarUrl());
        existingUser.setOccupation(userDetails.getOccupation());
        existingUser.setBio(userDetails.getBio());
        //Далі додаткова інформація
        /*
        existingUser.setSleepSchedule(userDetails.getSleepSchedule());
        existingUser.setGuestsFrequency(userDetails.getGuestsFrequency());
        existingUser.setCleanlinessLevel(userDetails.getCleanlinessLevel());
        existingUser.setDietaryPreferences(userDetails.getDietaryPreferences());
        existingUser.setNoiseTolerance(userDetails.getNoiseTolerance());
        existingUser.setLifestyleFlags(userDetails.getLifestyleFlags());
        */

        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    // PATCH /api/users/{id}/preferences - Частково оновити налаштування сусідства
    @PatchMapping("/{id}/preferences")
    public ResponseEntity<User> updateUserPreferences(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        if (updates.containsKey("sleep_schedule")) {
            existingUser.setSleepSchedule((String) updates.get("sleep_schedule"));
        }
        if (updates.containsKey("guests_frequency")) {
            existingUser.setGuestsFrequency((String) updates.get("guests_frequency"));
        }
        if (updates.containsKey("noise_tolerance")) {
            existingUser.setNoiseTolerance((String) updates.get("noise_tolerance"));
        }
        if (updates.containsKey("cleanliness_level")) {
            existingUser.setCleanlinessLevel((String) updates.get("cleanliness_level"));
        }
        if (updates.containsKey("dietary_preferences")) {
            existingUser.setDietaryPreferences((String) updates.get("dietary_preferences"));
        }
        if (updates.containsKey("lifestyle_flags")) {
            existingUser.setLifestyleFlags((String) updates.get("lifestyle_flags"));
        }

        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE /api/users/{id} - Видалити акаунт
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        userRepository.delete(existingUser);
        return ResponseEntity.noContent().build(); // Повертаємо 204 No Content (успішне видалення без тіла відповіді)
    }
}