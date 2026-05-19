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
import com.roomies.backend.exceptions.UnauthorizedAccessException;

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

    public Page<RoommateRequestDto> getRequestsByUserId(UUID userId, Pageable pageable) {
        return requestRepository.findByAuthorId(userId, pageable)
                .map(this::convertToDto);
    }

    public Page<RoommateRequestDto> getMyRequests(Pageable pageable) {
        User me = securityUtils.getCurrentUser();
        return getRequestsByUserId(me.getId(), pageable);
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
        dto.setIsActive(req.getIsActive());
        dto.setCreatedAt(req.getCreatedAt());
        dto.setStatus(req.getIsActive() != null && req.getIsActive() ? "ACTIVE" : "ARCHIVED");

        if (req.getTargetCity() != null) {
            dto.setTargetCityId(req.getTargetCity().getId());
            dto.setTargetCityName(req.getTargetCity().getName());
        }

        // --- ТУТ МАПИМО АВТОРА ТА НОВІ ПОЛЯ ---
        if (req.getAuthor() != null) {
            dto.setAuthorId(req.getAuthor().getId());
            dto.setAuthorFirstName(req.getAuthor().getFirstName());
            dto.setAuthorBirthDate(req.getAuthor().getBirthDate());
            dto.setAuthorAvatarUrl(req.getAuthor().getAvatarUrl());
            dto.setAuthorOccupation(req.getAuthor().getOccupation());
            dto.setAuthorGender(req.getAuthor().getGender());
            dto.setLifestyleProfile(req.getAuthor().getLifestyleProfile());
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
                dto.setAuthorId(req.getAuthor().getId()); 
                dto.setAuthorFirstName(req.getAuthor().getFirstName());
                dto.setAuthorBirthDate(req.getAuthor().getBirthDate());
                dto.setAuthorOccupation(req.getAuthor().getOccupation());

                dto.setAuthorGender(req.getAuthor().getGender());
                dto.setAuthorAvatarUrl(req.getAuthor().getAvatarUrl());
                dto.setLifestyleProfile(req.getAuthor().getLifestyleProfile());
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

    // Метод для фільтрації анкет за допомогою RoommateFilterDto
    // Метод для фільтрації анкет за допомогою RoommateFilterDto
    public List<RoommateRequestDto> searchRequests(RoommateFilterDto filter) {
        // 1. Шукаємо базу (бюджет, місто, вік, стать)
        Specification<RoommateRequest> spec = RoommateRequestSpecification.withFilter(filter);
        List<RoommateRequest> requests = requestRepository.findAll(spec);

        // 2. Фільтруємо Лайфстайл (JSONB) у пам'яті
        return requests.stream().filter(req -> {

                    // Перевіряємо, чи юзер взагалі застосував хоч один фільтр зі звичок або тварин
                    boolean hasLifestyleFilters = filter.getIsSmoker() != null ||
                            filter.getPartyHabits() != null ||
                            filter.getDrinksAlcohol() != null ||
                            (filter.getSleepSchedule() != null && !filter.getSleepSchedule().isEmpty()) ||
                            (filter.getNoiseTolerance() != null && !filter.getNoiseTolerance().isEmpty()) ||
                            (filter.getGuestsFrequency() != null && !filter.getGuestsFrequency().isEmpty()) ||
                            (filter.getCleanlinessLevel() != null && !filter.getCleanlinessLevel().isEmpty()) ||
                            (filter.getDietaryPreferences() != null && !filter.getDietaryPreferences().isEmpty()) ||
                            filter.getHasPets() != null;

                    if (req.getAuthor() == null || req.getAuthor().getLifestyleProfile() == null) {
                        // Якщо автор анкети не заповнив звички, але ми шукаємо саме за звичками -> відхиляємо його (!hasLifestyleFilters)
                        // Якщо ми не шукаємо за звичками -> пропускаємо (true)
                        return !hasLifestyleFilters;
                    }

                    var lifestyle = req.getAuthor().getLifestyleProfile();

                    if (filter.getIsSmoker() != null && !filter.getIsSmoker().equals(lifestyle.getIsSmoker())) return false;
                    if (filter.getPartyHabits() != null && !filter.getPartyHabits().equals(lifestyle.getPartyHabits())) return false;
                    if (filter.getDrinksAlcohol() != null && !filter.getDrinksAlcohol().equals(lifestyle.getDrinksAlcohol())) return false;

                    if (filter.getSleepSchedule() != null && !filter.getSleepSchedule().isEmpty() && !filter.getSleepSchedule().equals(lifestyle.getSleepSchedule())) return false;
                    if (filter.getNoiseTolerance() != null && !filter.getNoiseTolerance().isEmpty() && !filter.getNoiseTolerance().equals(lifestyle.getNoiseTolerance())) return false;
                    if (filter.getGuestsFrequency() != null && !filter.getGuestsFrequency().isEmpty() && !filter.getGuestsFrequency().equals(lifestyle.getGuestsFrequency())) return false;
                    if (filter.getCleanlinessLevel() != null && !filter.getCleanlinessLevel().isEmpty() && !filter.getCleanlinessLevel().equals(lifestyle.getCleanlinessLevel())) return false;
                    if (filter.getDietaryPreferences() != null && !filter.getDietaryPreferences().isEmpty() && !filter.getDietaryPreferences().equals(lifestyle.getDietaryPreferences())) return false;

                    // ДОДАНО: Правильна логіка перевірки наявності тварин
                    if (filter.getHasPets() != null) {
                        // Вважаємо, що тварина є, якщо поле не порожнє і не дорівнює "none"
                        boolean userHasPet = lifestyle.getPet() != null &&
                                !lifestyle.getPet().trim().isEmpty() &&
                                !lifestyle.getPet().equalsIgnoreCase("none");

                        if (filter.getHasPets() != userHasPet) return false;
                    }

                    return true;
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
        result.setAuthorOccupation(saved.getAuthor().getOccupation());
        result.setTargetCityId(saved.getTargetCity().getId());
        result.setTargetCityName(saved.getTargetCity().getName());
        
        result.setAuthorGender(saved.getAuthor().getGender());
        result.setAuthorAvatarUrl(saved.getAuthor().getAvatarUrl());
        result.setLifestyleProfile(saved.getAuthor().getLifestyleProfile());
        
        return result;
    }

    @Transactional
    public void archiveRequest(UUID id) {
        RoommateRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Анкету не знайдено"));
        
        User currentUser = securityUtils.getCurrentUser();
        if (!request.getAuthor().getId().equals(currentUser.getId()) && !currentUser.getRole().name().equals("ADMIN")) {
            throw new UnauthorizedAccessException("Ви не маєте доступу до цієї анкети");
        }
        
        request.setIsActive(false);
        requestRepository.save(request);
    }

    @Transactional
    public void unarchiveRequest(UUID id) {
        RoommateRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Анкету не знайдено"));
        
        User currentUser = securityUtils.getCurrentUser();
        if (!request.getAuthor().getId().equals(currentUser.getId()) && !currentUser.getRole().name().equals("ADMIN")) {
            throw new UnauthorizedAccessException("Ви не маєте доступу до цієї анкети");
        }
        
        request.setIsActive(true);
        requestRepository.save(request);
    }

}