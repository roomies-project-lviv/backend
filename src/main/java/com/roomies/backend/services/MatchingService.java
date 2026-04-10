package com.roomies.backend.services;

import com.roomies.backend.dto.RoommateRequestDto;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.RoommateMatchProjection;
import com.roomies.backend.repositories.RoommateRequestRepository;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MatchingService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoommateRequestRepository requestRepository;

    public Page<RoommateRequestDto> getPotentialMatches(UUID searcherId, Long cityId, Pageable pageable) {

        // 1. Отримуємо профіль того, хто шукає, щоб знати його звички
        User searcher = userRepository.findById(searcherId)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        // 2. Викликаємо наш JPQL запит з лімітами (пагінацією)
        Page<RoommateMatchProjection> matches = requestRepository.findPotentialMatches(
                cityId,
                searcherId,
                searcher.getLifestyleFlags(),
                searcher.getSleepSchedule(),
                searcher.getCleanlinessLevel(),
                searcher.getNoiseTolerance(),
                searcher.getGuestsFrequency(),
                searcher.getDietaryPreferences(),
                pageable
        );

        // 3. Конвертуємо результат у DTO
        return matches.map(projection -> {
            RoommateRequestDto dto = convertToDto(projection.getRequest());
            dto.setMatchPercentage(projection.getMatchPercentage());
            return dto;
        });
    }

    private RoommateRequestDto convertToDto(com.roomies.backend.models.RoommateRequest entity) {
        RoommateRequestDto dto = new RoommateRequestDto();
        dto.setId(entity.getId());
        dto.setBudgetMax(entity.getBudgetMax());
        dto.setMoveInDate(entity.getMoveInDate());
        dto.setRequirements(entity.getRequirements());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getAuthor() != null) {
            dto.setAuthorId(entity.getAuthor().getId());
            dto.setAuthorFirstName(entity.getAuthor().getFirstName());
            dto.setAuthorAvatarUrl(entity.getAuthor().getAvatarUrl());
        }

        if (entity.getTargetCity() != null) {
            dto.setTargetCityId(entity.getTargetCity().getId()); // Якщо Long
            dto.setTargetCityName(entity.getTargetCity().getName());
        }
        return dto;
    }
}