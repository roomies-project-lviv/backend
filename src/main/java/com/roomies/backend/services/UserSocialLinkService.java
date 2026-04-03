package com.roomies.backend.services;

import com.roomies.backend.dto.SocialLinkDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.User;
import com.roomies.backend.models.UserSocialLink;
import com.roomies.backend.repositories.UserSocialLinkRepository;
import com.roomies.backend.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserSocialLinkService {

    @Autowired private UserSocialLinkRepository linkRepository;
    @Autowired private SecurityUtils securityUtils; // <--- Додали SecurityUtils

    // Отримати СВОЇ лінки
    public List<SocialLinkDto> getMyLinks() {
        User me = securityUtils.getCurrentUser();
        return linkRepository.findByUserId(me.getId())
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Додати лінк СОБІ
    public SocialLinkDto addMyLink(SocialLinkDto dto) {
        User me = securityUtils.getCurrentUser();

        UserSocialLink newLink = new UserSocialLink();
        newLink.setPlatformName(dto.getPlatformName());
        newLink.setUrl(dto.getUrl());
        newLink.setUser(me); // Прив'язуємо до того, хто робить запит

        return convertToDto(linkRepository.save(newLink));
    }

    // Оновити СВІЙ лінк
    public SocialLinkDto updateMyLink(UUID linkId, SocialLinkDto dto) {
        User me = securityUtils.getCurrentUser();
        UserSocialLink existingLink = linkRepository.findById(linkId)
                .orElseThrow(() -> new ResourceNotFoundException("Посилання не знайдено"));

        // ЗАХИСТ ВІД IDOR: Перевіряємо, чи цей лінк належить поточному юзеру
        if (!existingLink.getUser().getId().equals(me.getId())) {
            throw new RuntimeException("Ви не маєте права редагувати це посилання");
        }

        existingLink.setPlatformName(dto.getPlatformName());
        existingLink.setUrl(dto.getUrl());

        return convertToDto(linkRepository.save(existingLink));
    }

    // Видалити СВІЙ лінк
    public void deleteMyLink(UUID linkId) {
        User me = securityUtils.getCurrentUser();
        UserSocialLink existingLink = linkRepository.findById(linkId)
                .orElseThrow(() -> new ResourceNotFoundException("Посилання не знайдено"));

        if (!existingLink.getUser().getId().equals(me.getId())) {
            throw new RuntimeException("Ви не маєте права видаляти це посилання");
        }

        linkRepository.delete(existingLink);
    }

    private SocialLinkDto convertToDto(UserSocialLink entity) {
        return new SocialLinkDto(entity.getId(), entity.getPlatformName(), entity.getUrl());
    }

    
}