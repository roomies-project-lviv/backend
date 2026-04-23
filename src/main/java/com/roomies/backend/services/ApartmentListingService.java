package com.roomies.backend.services;

import com.roomies.backend.dto.AdminListingSaveDto;
import com.roomies.backend.dto.ApartmentListingCreateDto;
import com.roomies.backend.dto.ApartmentListingDto;
import com.roomies.backend.dto.filters.ListingFilterDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.ApartmentListing;
import com.roomies.backend.models.City;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.ApartmentListingRepository;
import com.roomies.backend.repositories.CityRepository;
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
    @Autowired private CityRepository cityRepository;

    @Autowired private SecurityUtils securityUtils;

    public Page<ApartmentListingDto> getAllActiveListings(Pageable pageable) {
        return listingRepository.findByIsActiveTrue(pageable).map(this::convertToDto);
    }

    public ApartmentListingDto getListingById(UUID id) {
        ApartmentListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"));
        return convertToDto(listing);
    }

    @Transactional
    public ApartmentListingDto createListing(ApartmentListingCreateDto dto, User author) {
        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("Місто не знайдено"));

        ApartmentListing listing = new ApartmentListing();
        listing.setTitle(dto.getTitle());
        listing.setPricePerMonth(dto.getPricePerMonth());
        listing.setAddress(dto.getAddress());
        listing.setArea(dto.getArea());
        listing.setRoomsTotal(dto.getRoomsTotal());
        listing.setCity(city);
        listing.setAuthor(author);

        return convertToDto(listingRepository.save(listing));
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
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        
        // НОВЕ МАПЛЕННЯ (Без getApartment())
        dto.setAddress(entity.getAddress());
        dto.setArea(entity.getArea());
        dto.setRoomsTotal(entity.getRoomsTotal());
        
        if (entity.getCity() != null) {
            dto.setCityId(entity.getCity().getId());
            dto.setCityName(entity.getCity().getName());
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
            
            // ДОДАНО: Передаємо ID автора для фронтенду, щоб працював випадаючий список!
            if (listing.getAuthor() != null) {
                dto.setAuthorId(listing.getAuthor().getId()); 
                dto.setAuthorFirstName(listing.getAuthor().getFirstName());
            }
            
            dto.setAddress(listing.getAddress());
            dto.setArea(listing.getArea());
            dto.setRoomsTotal(listing.getRoomsTotal());
            
            if (listing.getCity() != null) {
                dto.setCityId(listing.getCity().getId());
                dto.setCityName(listing.getCity().getName());
            }
            
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
        ApartmentListing listing = (id != null && !id.isEmpty())
                ? listingRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"))
                : new ApartmentListing();

        User author = userRepository.findById(dto.getAuthorId()).orElseThrow(() -> new ResourceNotFoundException("Автора не знайдено"));
        City city = cityRepository.findById(dto.getCityId()).orElseThrow(() -> new ResourceNotFoundException("Місто не знайдено"));

        listing.setTitle(dto.getTitle());
        listing.setPricePerMonth(dto.getPricePerMonth());
        listing.setAddress(dto.getAddress());
        listing.setArea(dto.getArea());
        listing.setRoomsTotal(dto.getRoomsTotal());
        listing.setAuthor(author);
        listing.setCity(city);

        return convertToDto(listingRepository.save(listing));
    }

    // Метод для отримання фільтрованого списку
    public Page<ApartmentListingDto> getFilteredListings(ListingFilterDto filter, Pageable pageable) {
        Specification<ApartmentListing> spec = ApartmentListingSpecification.withFilter(filter);
        return listingRepository.findAll(spec, pageable)
                .map(this::convertToDto);
    }
}