package com.roomies.backend.services;

import com.roomies.backend.dto.RoommateRequestDto;
import com.roomies.backend.models.LifestyleProfile;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.RoommateMatchProjection;
import com.roomies.backend.repositories.RoommateRequestRepository;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.roomies.backend.exceptions.ResourceNotFoundException;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MatchingService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoommateRequestRepository requestRepository;

    public Page<RoommateRequestDto> getPotentialMatches(UUID searcherId, Long cityId, Pageable pageable) {
        User searcher = userRepository.findById(searcherId)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        LifestyleProfile profile = searcher.getLifestyleProfile();

        // 1. Захист від NullPointerException (якщо користувач не заповнив звички)
        Boolean isSmoker = profile != null ? profile.getIsSmoker() : null;
        Boolean drinksAlcohol = profile != null ? profile.getDrinksAlcohol() : null;
        Boolean partyHabits = profile != null ? profile.getPartyHabits() : null;
        String sleep = profile != null ? profile.getSleepSchedule() : null;
        String clean = profile != null ? profile.getCleanlinessLevel() : null;
        String noise = profile != null ? profile.getNoiseTolerance() : null;
        String guests = profile != null ? profile.getGuestsFrequency() : null;
        String diet = profile != null ? profile.getDietaryPreferences() : null;

        // 2. Викликаємо Native SQL запит, передаючи параметри окремо
        Page<RoommateMatchProjection> matches = requestRepository.findPotentialMatches(
                cityId,
                searcherId,
                isSmoker,
                drinksAlcohol,
                partyHabits,
                sleep,
                clean,
                noise,
                guests,
                diet,
                pageable
        );

        // 3. Збираємо фінальний результат
        return matches.map(projection -> {
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
            dto.setTargetCityId(entity.getTargetCity().getId());
            dto.setTargetCityName(entity.getTargetCity().getName());
        }
        return dto;
    }
}