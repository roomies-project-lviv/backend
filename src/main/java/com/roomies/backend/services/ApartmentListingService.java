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

import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import com.roomies.backend.exceptions.UnauthorizedAccessException;

@Service
@Transactional
public class ApartmentListingService {

    @Autowired
    private ApartmentListingRepository listingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private SecurityUtils securityUtils;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Autowired
    private SupabaseStorageService storageService;

    public Page<ApartmentListingDto> getAllActiveListings(Pageable pageable) {
        return listingRepository.findByIsActiveTrue(pageable).map(this::convertToDto);
    }

    public ApartmentListingDto getListingById(UUID id) {
        ApartmentListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"));
        return convertToDto(listing);
    }

    public Page<ApartmentListingDto> getListingsByUserId(UUID userId, Pageable pageable) {
        return listingRepository.findByAuthorId(userId, pageable)
                .map(this::convertToDto);
    }

    public Page<ApartmentListingDto> getMyListings(Pageable pageable) {
        User me = securityUtils.getCurrentUser();
        return getListingsByUserId(me.getId(), pageable);
    }

    @Transactional
    public ApartmentListingDto createListing(ApartmentListingCreateDto dto, User author, List<MultipartFile> images) {
        
        String fullAddress = dto.getAddress();
        String[] addressParts = fullAddress.split(",", 2); 

        if (addressParts.length < 2) {
            throw new IllegalArgumentException("Неправильний формат адреси. Очікується формат: Місто, Вулиця...");
        }

        String rawCityName = addressParts[0].trim();
        String cleanCityName = rawCityName.replaceAll("(?i)^(м\\.?|місто)\\s*", "").trim();

        String streetAddress = addressParts[1].trim();

        City city = cityRepository.findByNameIgnoreCase(cleanCityName)
                .orElseThrow(() -> new ResourceNotFoundException("Місто '" + cleanCityName + "' не знайдено в нашій базі"));


        ApartmentListing listing = new ApartmentListing();
        listing.setTitle(dto.getTitle());
        listing.setPricePerMonth(dto.getPricePerMonth());
        
        listing.setAddress(streetAddress); 
        
        listing.setArea(dto.getArea());
        listing.setRoomsTotal(dto.getRoomsTotal());
        
        listing.setCity(city); 
        
        listing.setApartmentType(dto.getApartmentType());
        listing.setAuthor(author);
        listing.setAmenities(dto.getAmenities());

        if (dto.getLatitude() != null && dto.getLongitude() != null) {
            Point location = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
            listing.setLocation(location);
        }

        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = storageService.uploadListingImages(images);
            listing.setImageUrls(imageUrls);
        }

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
        dto.setStatus(entity.getIsActive() != null && entity.getIsActive() ? "ACTIVE" : "ARCHIVED");

        dto.setAddress(entity.getAddress());
        dto.setArea(entity.getArea());
        dto.setRoomsTotal(entity.getRoomsTotal());
        dto.setApartmentType(entity.getApartmentType());
        dto.setAmenities(entity.getAmenities());

        if (entity.getCity() != null) {
            dto.setCityId(entity.getCity().getId());
            dto.setCityName(entity.getCity().getName());
        }

        if (entity.getLocation() != null) {
            // У JTS Point: X - це довгота (longitude), Y - це широта (latitude)
            dto.setLongitude(entity.getLocation().getX());
            dto.setLatitude(entity.getLocation().getY());
        }
        dto.setImageUrls(entity.getImageUrls());
        return dto;
    }

    @Transactional
    public ApartmentListingDto updateListing(UUID id, ApartmentListingCreateDto dto, List<String> retainedImages,
            List<MultipartFile> newImages) {
        ApartmentListing existing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"));

        existing.setTitle(dto.getTitle());
        existing.setPricePerMonth(dto.getPricePerMonth());
        existing.setArea(dto.getArea());
        existing.setAddress(dto.getAddress());
        existing.setRoomsTotal(dto.getRoomsTotal());
        existing.setApartmentType(dto.getApartmentType());
        existing.setAmenities(dto.getAmenities());

        if (dto.getCityId() != 0) {
            City city = cityRepository.findById(dto.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Місто не знайдено"));
            existing.setCity(city);
        }

        if (dto.getLatitude() != null && dto.getLongitude() != null) {
            Point location = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
            existing.setLocation(location);
        }

        // --- ОЧИЩЕННЯ ФОТОГРАФІЙ ---
        List<String> currentImages = existing.getImageUrls();
        if (currentImages != null) {
            for (String currentImage : currentImages) {
                // Якщо стара фотографія НЕ передана у списку залишених (retainedImages),
                // видаляємо її зі сховища
                if (retainedImages == null || !retainedImages.contains(currentImage)) {
                    storageService.deleteListingImage(currentImage);
                }
            }
        }

        // Формуємо фінальний список картинок (починаємо з тих, що вирішили залишити)
        List<String> finalImages = new java.util.ArrayList<>();
        if (retainedImages != null) {
            finalImages.addAll(retainedImages);
        }

        // Завантажуємо абсолютно нові фотографії, якщо вони є
        if (newImages != null && !newImages.isEmpty()) {
            List<String> uploadedUrls = storageService.uploadListingImages(newImages);
            finalImages.addAll(uploadedUrls);
        }

        // Оновлюємо масив у базі даних
        existing.setImageUrls(finalImages);

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
            dto.setApartmentType(listing.getApartmentType());
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
                ? listingRepository.findById(UUID.fromString(id))
                        .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"))
                : new ApartmentListing();

        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Автора не знайдено"));
        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("Місто не знайдено"));

        listing.setTitle(dto.getTitle());
        listing.setPricePerMonth(dto.getPricePerMonth());
        listing.setAddress(dto.getAddress());
        listing.setArea(dto.getArea());
        listing.setRoomsTotal(dto.getRoomsTotal());
        listing.setAuthor(author);
        listing.setCity(city);
        listing.setApartmentType(dto.getApartmentType());

        return convertToDto(listingRepository.save(listing));
    }

    // Метод для отримання фільтрованого списку
    public Page<ApartmentListingDto> getFilteredListings(ListingFilterDto filter, Pageable pageable) {
        Specification<ApartmentListing> spec = ApartmentListingSpecification.withFilter(filter);
        return listingRepository.findAll(spec, pageable)
                .map(this::convertToDto);
    }

    @Transactional
    public void archiveListing(UUID id) {
        ApartmentListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"));

        User currentUser = securityUtils.getCurrentUser();
        // Перевіряємо, чи є користувач автором або адміном
        if (!listing.getAuthor().getId().equals(currentUser.getId()) && !currentUser.getRole().name().equals("ADMIN")) {
            throw new UnauthorizedAccessException("Ви не маєте доступу до цього оголошення");
        }

        listing.setIsActive(false); // Змінюємо статус
        listingRepository.save(listing);
    }

    @Transactional
    public void unarchiveListing(UUID id) {
        ApartmentListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"));

        User currentUser = securityUtils.getCurrentUser();
        if (!listing.getAuthor().getId().equals(currentUser.getId()) && !currentUser.getRole().name().equals("ADMIN")) {
            throw new UnauthorizedAccessException("Ви не маєте доступу до цього оголошення");
        }

        listing.setIsActive(true); // Повертаємо статус
        listingRepository.save(listing);
    }
}
