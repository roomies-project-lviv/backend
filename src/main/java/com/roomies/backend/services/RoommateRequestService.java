package com.roomies.backend.services;

import com.roomies.backend.dto.RoommateRequestCreateDto;
import com.roomies.backend.dto.RoommateRequestDto;
import com.roomies.backend.models.City;
import com.roomies.backend.models.RoommateRequest;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.CityRepository;
import com.roomies.backend.repositories.RoommateRequestRepository;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoommateRequestService {

    @Autowired private RoommateRequestRepository requestRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CityRepository cityRepository;

    public List<RoommateRequestDto> getAllActiveRequests() {
        return requestRepository.findByIsActiveTrue()
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public RoommateRequestDto getRequestById(UUID id) {
        RoommateRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Анкету не знайдено"));
        return convertToDto(request);
    }

    public RoommateRequestDto createRequest(RoommateRequestCreateDto dto) {
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        RoommateRequest request = new RoommateRequest();
        request.setAuthor(author);
        
        if (dto.getTargetCityId() != null) {
            City city = cityRepository.findById(dto.getTargetCityId())
                    .orElseThrow(() -> new RuntimeException("Місто не знайдено"));
            request.setTargetCity(city);
        }

        request.setBudgetMax(dto.getBudgetMax());
        request.setMoveInDate(dto.getMoveInDate());
        request.setRequirements(dto.getRequirements());

        RoommateRequest saved = requestRepository.save(request);
        return convertToDto(saved);
    }
    
    public RoommateRequestDto updateRequest(UUID id, RoommateRequestCreateDto dto) {
        RoommateRequest existing = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Анкету не знайдено"));

        existing.setBudgetMax(dto.getBudgetMax());
        existing.setMoveInDate(dto.getMoveInDate());
        existing.setRequirements(dto.getRequirements());

        if (dto.getTargetCityId() != null) {
            City city = cityRepository.findById(dto.getTargetCityId())
                    .orElseThrow(() -> new RuntimeException("Місто не знайдено"));
            existing.setTargetCity(city);
        } else {
            existing.setTargetCity(null);
        }

        RoommateRequest updated = requestRepository.save(existing);
        return convertToDto(updated);
    }

    public void deleteRequest(UUID id) {
        requestRepository.deleteById(id);
    }

    // --- Перекладач (Mapper) ---
    private RoommateRequestDto convertToDto(RoommateRequest entity) {
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