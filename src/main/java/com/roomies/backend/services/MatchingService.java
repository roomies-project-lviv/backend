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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomies.backend.exceptions.ResourceNotFoundException;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MatchingService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoommateRequestRepository requestRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    public Page<RoommateRequestDto> getPotentialMatches(UUID searcherId, Long cityId, Pageable pageable) {
        User searcher = userRepository.findById(searcherId)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        // 1. Перетворюємо Map у JSON-рядок для бази даних
        String lifestyleJsonString = "{}";
        try {
            if (searcher.getLifestyleFlags() != null) {
                lifestyleJsonString = objectMapper.writeValueAsString(searcher.getLifestyleFlags());
            }
        } catch (JsonProcessingException e) {
            System.err.println("Помилка конвертації JSON: " + e.getMessage());
        }

        // 2. Викликаємо Native SQL запит
        Page<RoommateMatchProjection> matches = requestRepository.findPotentialMatches(
                cityId,
                searcherId,
                lifestyleJsonString, // Передаємо сформований JSON
                searcher.getSleepSchedule(),
                searcher.getCleanlinessLevel(),
                searcher.getNoiseTolerance(),
                searcher.getGuestsFrequency(),
                searcher.getDietaryPreferences(),
                pageable
        );

        // 3. Збираємо фінальний результат
        return matches.map(projection -> {
            // Оскільки база віддала тільки ID, ми дістаємо повну анкету
            com.roomies.backend.models.RoommateRequest request = requestRepository.findById(projection.getRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Анкету не знайдено"));

            RoommateRequestDto dto = convertToDto(request);
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