package com.roomies.backend.services;

import com.roomies.backend.dto.AdminListingSaveDto;
import com.roomies.backend.dto.ApartmentListingCreateDto;
import com.roomies.backend.dto.ApartmentListingDto;
import com.roomies.backend.dto.filters.ListingFilterDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.Apartment;
import com.roomies.backend.models.ApartmentListing;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.ApartmentListingRepository;
import com.roomies.backend.repositories.ApartmentRepository;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.security.SecurityUtils;
import com.roomies.backend.specifications.ApartmentListingSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Service
@Transactional
public class ApartmentListingService {

    @Autowired private ApartmentListingRepository listingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ApartmentRepository apartmentRepository;

    @Autowired private SecurityUtils securityUtils;

    public Page<ApartmentListingDto> getAllActiveListings(Pageable pageable) {
        return listingRepository.findByIsActiveTrue(pageable).map(this::convertToDto);
    }

    public ApartmentListingDto getListingById(UUID id) {
        ApartmentListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"));
        return convertToDto(listing);
    }

    public ApartmentListingDto createListing(ApartmentListingCreateDto dto) {
        User author = securityUtils.getCurrentUser();
        
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

    public Page<ApartmentListingDto> getAllActiveListings(ListingFilterDto filterDto, Pageable pageable) {
        Specification<ApartmentListing> spec = ApartmentListingSpecification.withFilter(filterDto);
        return listingRepository.findAll(spec, pageable).map(this::convertToDto);
    }


    // Отримати всі оголошення для адмінки
    public List<ApartmentListingDto> getAllListingsForAdmin() {
        return listingRepository.findAll().stream().map(listing -> {
            ApartmentListingDto dto = new ApartmentListingDto();
            dto.setId(listing.getId());
            dto.setTitle(listing.getTitle());
            dto.setPricePerMonth(listing.getPricePerMonth());
            dto.setAuthorFirstName(listing.getAuthor().getFirstName());
            
            // Дістаємо назву міста через об'єкт Apartment
            String cityName = "";
            if (listing.getApartment() != null && listing.getApartment().getCity() != null) {
                cityName = listing.getApartment().getCity().getName() + ", ";
            }
            
            dto.setAddress(cityName + listing.getApartment().getAddress());
            
            return dto;
        }).collect(Collectors.toList());
    }

    // Видалити оголошення (модерація)
    @Transactional
    public void deleteListingByAdmin(String id) {
        // Конвертуємо String у UUID
        UUID uuidId = UUID.fromString(id); 

        // Тепер передаємо uuidId у репозиторій
        if (!listingRepository.existsById(uuidId)) {
            throw new ResourceNotFoundException("Оголошення не знайдено");
        }
        listingRepository.deleteById(uuidId);
    }

    @Transactional
    public ApartmentListingDto saveListingByAdmin(String id, AdminListingSaveDto dto) {
        ApartmentListing listing;
        
        // Якщо передали ID - шукаємо (Редагування), якщо ні - створюємо нове
        if (id != null && !id.isEmpty()) {
            listing = listingRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"));
        } else {
            listing = new ApartmentListing();
        }

        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено"));
                
        Apartment apartment = apartmentRepository.findById(dto.getApartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Квартиру не знайдено"));

        listing.setTitle(dto.getTitle());
        listing.setPricePerMonth(dto.getPricePerMonth());
        listing.setAuthor(author);
        listing.setApartment(apartment);

        listing = listingRepository.save(listing);
        return convertToDto(listing); // Використовуємо твій існуючий мапер
    }

}