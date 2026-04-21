package com.roomies.backend.services;

import com.roomies.backend.dto.AdminApartmentSaveDto;
import com.roomies.backend.dto.ApartmentCreateDto;
import com.roomies.backend.dto.ApartmentDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.Apartment;
import com.roomies.backend.models.City;
import com.roomies.backend.repositories.ApartmentRepository;
import com.roomies.backend.repositories.CityRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;
    private CityRepository cityRepository; 

    // 1. Отримати всі
    public List<ApartmentDto> getAllApartments() {
        return apartmentRepository.findAll()
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 2. Отримати одну за ID
    public ApartmentDto getApartmentById(UUID id) {
        Apartment appt = apartmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено з ID: " + id));
        return convertToDto(appt);
    }

    // 3. Створити нову
    public ApartmentDto createApartment(ApartmentCreateDto dto) {
        Apartment apartment = new Apartment();
        updateEntityFromDto(apartment, dto);
        Apartment saved = apartmentRepository.save(apartment);
        return convertToDto(saved);
    }

    // 4. Оновити існуючу
    public ApartmentDto updateApartment(UUID id, ApartmentCreateDto dto) {
        Apartment existing = apartmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено з ID: " + id));
        
        updateEntityFromDto(existing, dto);
        Apartment updated = apartmentRepository.save(existing);
        return convertToDto(updated);
    }

    // 5. Видалити
    public void deleteApartment(UUID id) {
        apartmentRepository.deleteById(id);
    }

    // --- Допоміжні методи ---
    private void updateEntityFromDto(Apartment entity, ApartmentCreateDto dto) {
        entity.setArea(dto.getArea());
        entity.setRoomsTotal(dto.getRoomsTotal());
        entity.setAddress(dto.getAddress());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setDescription(dto.getDescription());
        entity.setAvailableFrom(dto.getAvailableFrom());
    }

    private ApartmentDto convertToDto(Apartment entity) {
        ApartmentDto dto = new ApartmentDto();
        dto.setId(entity.getId());
        dto.setArea(entity.getArea());
        dto.setRoomsTotal(entity.getRoomsTotal());
        dto.setAddress(entity.getAddress());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setDescription(entity.getDescription());
        dto.setAvailableFrom(entity.getAvailableFrom());
        return dto;
    }

    public List<ApartmentDto> getAllApartmentsForAdmin() {
        return apartmentRepository.findAll().stream()
                .map(this::convertToDto) // Використовуємо твій існуючий мапер
                .collect(Collectors.toList());
    }

    @Transactional
    public ApartmentDto saveApartmentByAdmin(String id, AdminApartmentSaveDto dto) {
        Apartment apartment = (id != null && !id.isEmpty()) 
            ? apartmentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Квартиру не знайдено"))
            : new Apartment();

        City city = cityRepository.findById(dto.getCityId())
            .orElseThrow(() -> new ResourceNotFoundException("Місто не знайдено"));

        apartment.setAddress(dto.getAddress());
        apartment.setArea(dto.getArea());
        apartment.setRoomsTotal(dto.getRoomsTotal());
        apartment.setCity(city);

        return convertToDto(apartmentRepository.save(apartment));
    }

    @Transactional
    public void deleteApartmentByAdmin(String id) {
        apartmentRepository.deleteById(UUID.fromString(id));
    }
    
}