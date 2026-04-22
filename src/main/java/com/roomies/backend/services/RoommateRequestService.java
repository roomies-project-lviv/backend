package com.roomies.backend.services;

import com.roomies.backend.dto.RoommateRequestCreateDto;
import com.roomies.backend.dto.RoommateRequestDto;
import com.roomies.backend.dto.filters.RoommateFilterDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.City;
import com.roomies.backend.models.RoommateRequest;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.CityRepository;
import com.roomies.backend.repositories.RoommateRequestRepository;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.security.SecurityUtils;
import com.roomies.backend.specifications.RoommateRequestSpecification;
import com.roomies.backend.dto.AdminRequestSaveDto;
import com.roomies.backend.models.City;
import com.roomies.backend.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.roomies.backend.dto.filters.RoommateFilterDto;
import com.roomies.backend.specifications.RoommateRequestSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoommateRequestService {

    @Autowired private RoommateRequestRepository requestRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private RoommateRequestRepository roommateRequestRepository;

    @Autowired private SecurityUtils securityUtils;

    public List<RoommateRequestDto> getAllActiveRequests() {
        return requestRepository.findByIsActiveTrue()
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public RoommateRequestDto getRequestById(UUID id) {
        RoommateRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Анкету не знайдено"));
        return convertToDto(request);
    }

    public RoommateRequestDto createRequest(RoommateRequestCreateDto dto) {
        User author = securityUtils.getCurrentUser();

        RoommateRequest request = new RoommateRequest();
        request.setAuthor(author);
        
        if (dto.getTargetCityId() != null) {
            City city = cityRepository.findById(dto.getTargetCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Місто не знайдено"));
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
                .orElseThrow(() -> new ResourceNotFoundException("Анкету не знайдено"));

        existing.setBudgetMax(dto.getBudgetMax());
        existing.setMoveInDate(dto.getMoveInDate());
        existing.setRequirements(dto.getRequirements());

        if (dto.getTargetCityId() != null) {
            City city = cityRepository.findById(dto.getTargetCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Місто не знайдено"));
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
    private RoommateRequestDto convertToDto(RoommateRequest req) {
        RoommateRequestDto dto = new RoommateRequestDto();
        dto.setId(req.getId());
        dto.setBudgetMax(req.getBudgetMax());
        dto.setRequirements(req.getRequirements());
        
        if (req.getTargetCity() != null) {
            dto.setTargetCityId(req.getTargetCity().getId());
            dto.setTargetCityName(req.getTargetCity().getName());
        }
        
        // --- ТУТ МАПИМО АВТОРА ТА НОВІ ПОЛЯ ---
        if (req.getAuthor() != null) {
            dto.setAuthorId(req.getAuthor().getId());
            dto.setAuthorFirstName(req.getAuthor().getFirstName());
            
            // 1. Витягуємо дату народження напряму з User
            dto.setAuthorBirthDate(req.getAuthor().getBirthDate());
            
            // 2. Витягуємо професію з LifestyleProfile (якщо профіль створено)
            if (req.getAuthor().getLifestyleProfile() != null) {
                dto.setAuthorOccupation(req.getAuthor().getOccupation());
            }
        }
        
        return dto;
    }

    public Page<RoommateRequestDto> getAllActiveRequests(RoommateFilterDto filterDto, Pageable pageable) {
        Specification<RoommateRequest> spec = RoommateRequestSpecification.withFilter(filterDto);
        return requestRepository.findAll(spec, pageable).map(this::convertToDto);
    }


    // Отримати всі анкети для адмінки
    public List<RoommateRequestDto> getAllRequestsForAdmin() {
        return requestRepository.findAll().stream().map(req -> {
            RoommateRequestDto dto = new RoommateRequestDto();
            dto.setId(req.getId());
            dto.setBudgetMax(req.getBudgetMax());
            dto.setRequirements(req.getRequirements()); // ДОДАНО
            
            if (req.getTargetCity() != null) {
                dto.setTargetCityId(req.getTargetCity().getId()); // ВАЖЛИВО ДЛЯ ФОРМИ!
                dto.setTargetCityName(req.getTargetCity().getName());
            }
            if (req.getAuthor() != null) {
                dto.setAuthorId(req.getAuthor().getId()); // ВАЖЛИВО ДЛЯ ФОРМИ!
                dto.setAuthorFirstName(req.getAuthor().getFirstName());
                dto.setAuthorBirthDate(req.getAuthor().getBirthDate());
                if (req.getAuthor().getLifestyleProfile() != null) {
                    dto.setAuthorOccupation(req.getAuthor().getOccupation());
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // Видалити анкету
    @Transactional
    public void deleteRequestByAdmin(String id) {
        // Конвертуємо String у UUID
        UUID uuidId = UUID.fromString(id);
        if (!requestRepository.existsById(uuidId)) {
            throw new ResourceNotFoundException("Анкету не знайдено");
        }
        requestRepository.deleteById(uuidId);
    }

    @Transactional
    public RoommateRequestDto saveRequestByAdmin(String id, AdminRequestSaveDto dto) {
        RoommateRequest request = (id != null && !id.isEmpty())
            ? roommateRequestRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Анкету не знайдено"))
            : new RoommateRequest();

        User author = userRepository.findById(dto.getAuthorId()).orElseThrow(() -> new ResourceNotFoundException("Автора не знайдено"));
        City targetCity = cityRepository.findById(dto.getTargetCityId()).orElseThrow(() -> new ResourceNotFoundException("Місто не знайдено"));

        request.setRequirements(dto.getRequirements());
        request.setAuthor(author);
        request.setBudgetMax(dto.getBudgetMax());
        request.setTargetCity(targetCity);

        RoommateRequest saved = roommateRequestRepository.save(request);
        
        RoommateRequestDto result = new RoommateRequestDto();
        result.setId(saved.getId());
        result.setBudgetMax(saved.getBudgetMax());
        result.setRequirements(saved.getRequirements());
        result.setAuthorId(saved.getAuthor().getId());
        result.setAuthorFirstName(saved.getAuthor().getFirstName());
        result.setAuthorBirthDate(saved.getAuthor().getBirthDate()); 
        if (saved.getAuthor().getLifestyleProfile() != null) {
            result.setAuthorOccupation(saved.getAuthor().getOccupation()); 
        }
        result.setTargetCityId(saved.getTargetCity().getId());
        result.setTargetCityName(saved.getTargetCity().getName());
        
        return result;
    }

}