package com.roomies.backend.services;

import com.roomies.backend.dto.SocialLinkDto;
import com.roomies.backend.models.User;
import com.roomies.backend.models.UserSocialLink;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.repositories.UserSocialLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service // Кажемо Spring, що це наш шар бізнес-логіки
public class UserSocialLinkService {

    @Autowired
    private UserSocialLinkRepository linkRepository;

    @Autowired
    private UserRepository userRepository;

    // --- МЕТОДИ ДЛЯ РОБОТИ З БАЗОЮ ТА DTO ---

    public List<SocialLinkDto> getUserLinks(UUID userId) {
        List<UserSocialLink> links = linkRepository.findByUserId(userId);
        
        // Перетворюємо список Entity на список DTO
        return links.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SocialLinkDto addLink(UUID userId, SocialLinkDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        // Створюємо нову сутність для бази
        UserSocialLink newLink = new UserSocialLink();
        newLink.setPlatformName(dto.getPlatformName());
        newLink.setUrl(dto.getUrl());
        newLink.setUser(user);

        // Зберігаємо в базу
        UserSocialLink savedLink = linkRepository.save(newLink);

        // Повертаємо фронтенду чистий DTO
        return convertToDto(savedLink);
    }

    public SocialLinkDto updateLink(UUID linkId, SocialLinkDto dto) {
        UserSocialLink existingLink = linkRepository.findById(linkId)
                .orElseThrow(() -> new RuntimeException("Посилання не знайдено"));

        existingLink.setPlatformName(dto.getPlatformName());
        existingLink.setUrl(dto.getUrl());

        UserSocialLink updatedLink = linkRepository.save(existingLink);
        return convertToDto(updatedLink);
    }

    public void deleteLink(UUID linkId) {
        linkRepository.deleteById(linkId);
    }

    // --- ДОПОМІЖНИЙ МЕТОД (ПЕРЕКЛАДАЧ) ---
    private SocialLinkDto convertToDto(UserSocialLink entity) {
        return new SocialLinkDto(entity.getId(), entity.getPlatformName(), entity.getUrl());
    }
    
}