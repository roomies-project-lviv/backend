package com.roomies.backend.controllers;

import com.roomies.backend.models.UserSocialLink;
import com.roomies.backend.repositories.UserSocialLinkRepository;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api") // Базовий шлях
public class UserSocialLinkController {

    @Autowired
    private UserSocialLinkRepository linkRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Отримати всі посилання конкретного користувача
    @GetMapping("/users/{userId}/social-links")
    public List<UserSocialLink> getUserLinks(@PathVariable UUID userId) {
        return linkRepository.findByUserId(userId);
    }

    // 2. Додати нове посилання користувачу
    @PostMapping("/users/{userId}/social-links")
    public UserSocialLink addLink(@PathVariable UUID userId, @RequestBody UserSocialLink newLink) {
        // Логіка: знайти користувача -> прикріпити до newLink -> зберегти (Михайло зможе дописати деталі)
        return null; // Поки що заглушка
    }
/*
// 2. Додати нове посилання користувачу
    @PostMapping("/users/{userId}/social-links")
    public UserSocialLink addLink(@PathVariable UUID userId, @RequestBody UserSocialLink newLink) {
        
        // 1. Шукаємо користувача в базі за його ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено з ID: " + userId));

        // 2. Прив'язуємо знайденного користувача до нового посилання
        newLink.setUser(user);

        // 3. Зберігаємо посилання в базу даних і повертаємо збережений результат
        return linkRepository.save(newLink);
    }
*/
    

    // 3. Видалити посилання
    @DeleteMapping("/social-links/{linkId}")
    public void deleteLink(@PathVariable UUID linkId) {
        linkRepository.deleteById(linkId);
    }

    // 4. Оновити існуюче посилання
    @PutMapping("/social-links/{linkId}")
    public UserSocialLink updateLink(@PathVariable UUID linkId, @RequestBody UserSocialLink updatedData) {
        
        // 1. Шукаємо старе посилання в базі
        UserSocialLink existingLink = linkRepository.findById(linkId)
                .orElseThrow(() -> new RuntimeException("Посилання не знайдено з ID: " + linkId));

        // 2. Оновлюємо дані на ті, що прийшли з Postman
        existingLink.setPlatformName(updatedData.getPlatformName());
        existingLink.setUrl(updatedData.getUrl());

        // 3. Зберігаємо оновлене посилання в базу
        return linkRepository.save(existingLink);
    }
    
}