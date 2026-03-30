package com.roomies.backend.services;

import com.roomies.backend.dto.ApartmentListingCreateDto;
import com.roomies.backend.dto.ApartmentListingDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.Apartment;
import com.roomies.backend.models.ApartmentListing;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.ApartmentListingRepository;
import com.roomies.backend.repositories.ApartmentRepository;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class ApartmentListingService {

    @Autowired private ApartmentListingRepository listingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ApartmentRepository apartmentRepository;

    public Page<ApartmentListingDto> getAllActiveListings(Pageable pageable) {
        return listingRepository.findByIsActiveTrue(pageable).map(this::convertToDto);
    }

    public ApartmentListingDto getListingById(UUID id) {
        ApartmentListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"));
        return convertToDto(listing);
    }

    public ApartmentListingDto createListing(ApartmentListingCreateDto dto) {
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Автора не знайдено"));
        
        Apartment apartment = apartmentRepository.findById(dto.getApartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Квартиру не знайдено"));

        ApartmentListing listing = new ApartmentListing();
        listing.setAuthor(author);
        listing.setApartment(apartment);
        listing.setTitle(dto.getTitle());
        listing.setPricePerMonth(dto.getPricePerMonth());
        
        ApartmentListing saved = listingRepository.save(listing);
        return convertToDto(saved);
    }

    public void deleteListing(UUID id) {
        listingRepository.deleteById(id);
    }

    // --- Перекладач (Mapper) ---
    private ApartmentListingDto convertToDto(ApartmentListing entity) {
        ApartmentListingDto dto = new ApartmentListingDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPricePerMonth(entity.getPricePerMonth());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getAuthor() != null) {
            dto.setAuthorId(entity.getAuthor().getId());
            dto.setAuthorFirstName(entity.getAuthor().getFirstName());
            dto.setAuthorAvatarUrl(entity.getAuthor().getAvatarUrl());
        }

        if (entity.getApartment() != null) {
            dto.setApartmentId(entity.getApartment().getId());
            dto.setAddress(entity.getApartment().getAddress());
            dto.setArea(entity.getApartment().getArea());
            dto.setRoomsTotal(entity.getApartment().getRoomsTotal());
        }

        return dto;
    }

    public ApartmentListingDto updateListing(UUID id, ApartmentListingCreateDto dto) {
        ApartmentListing existing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"));

        // Зазвичай при оновленні оголошення змінюють лише текст і ціну, 
        // але якщо треба, можна оновлювати й інші поля
        existing.setTitle(dto.getTitle());
        existing.setPricePerMonth(dto.getPricePerMonth());

        ApartmentListing updated = listingRepository.save(existing);
        return convertToDto(updated);
    }

    
}