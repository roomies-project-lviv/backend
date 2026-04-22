package com.roomies.backend.controllers;

import com.roomies.backend.dto.ApartmentListingDto;
import com.roomies.backend.dto.RoommateRequestDto;
import com.roomies.backend.dto.UserDto;
import com.roomies.backend.services.ApartmentListingService;
import com.roomies.backend.services.RoommateRequestService;
import com.roomies.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.roomies.backend.dto.CityDto;
import com.roomies.backend.dto.PetTypeDto;
import com.roomies.backend.services.CityService;
import com.roomies.backend.services.PetTypeService;
import com.roomies.backend.dto.AdminListingSaveDto;
import com.roomies.backend.dto.AdminRequestSaveDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") // МАГІЯ ТУТ: Тільки юзери з ROLE_ADMIN можуть сюди стукати
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private ApartmentListingService apartmentListingService;
    @Autowired private RoommateRequestService roommateRequestService;
    @Autowired private CityService cityService;
    @Autowired private PetTypeService petTypeService;

    // 1. Отримати список усіх користувачів
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersForAdmin());
    }

    // 2. Видалити користувача
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUserByAdmin(id);
        return ResponseEntity.ok().build();
    }

    // 3. Видалити чуже оголошення
    @DeleteMapping("/listings/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable String id) {
        apartmentListingService.deleteListingByAdmin(id);
        return ResponseEntity.ok().build();
    }

    // --- ОГОЛОШЕННЯ КВАРТИР ---
    @GetMapping("/listings")
    public ResponseEntity<List<ApartmentListingDto>> getAllListings() {
        return ResponseEntity.ok(apartmentListingService.getAllListingsForAdmin());
    }

    @PostMapping("/listings")
    public ResponseEntity<ApartmentListingDto> createListing(@RequestBody AdminListingSaveDto dto) {
        return ResponseEntity.ok(apartmentListingService.saveListingByAdmin(null, dto));
    }

    @PutMapping("/listings/{id}")
    public ResponseEntity<ApartmentListingDto> updateListing(@PathVariable String id, @RequestBody AdminListingSaveDto dto) {
        return ResponseEntity.ok(apartmentListingService.saveListingByAdmin(id, dto));
    }

    // --- АНКЕТИ СПІВМЕШКАНЦІВ ---
    @GetMapping("/roommate-requests")
    public ResponseEntity<List<RoommateRequestDto>> getAllRoommateRequests() {
        return ResponseEntity.ok(roommateRequestService.getAllRequestsForAdmin());
    }

    @DeleteMapping("/roommate-requests/{id}")
    public ResponseEntity<Void> deleteRoommateRequest(@PathVariable String id) {
        roommateRequestService.deleteRequestByAdmin(id);
        return ResponseEntity.ok().build();
    }

    // --- МІСТА (CITIES) ---
    @GetMapping("/cities")
    public ResponseEntity<List<CityDto>> getAllCities() { return ResponseEntity.ok(cityService.getAllCities()); }

    @PostMapping("/cities")
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto dto) { return ResponseEntity.ok(cityService.createCity(dto)); }

    @PutMapping("/cities/{id}")
    public ResponseEntity<CityDto> updateCity(@PathVariable int id, @RequestBody CityDto dto) { return ResponseEntity.ok(cityService.updateCity(id, dto)); }

    @DeleteMapping("/cities/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable int id) { cityService.deleteCity(id); return ResponseEntity.ok().build(); }

    // --- ТИПИ ТВАРИН (PET TYPES) ---
    @GetMapping("/pet-types")
    public ResponseEntity<List<PetTypeDto>> getAllPetTypes() { return ResponseEntity.ok(petTypeService.getAllPetTypes()); }

    @PostMapping("/pet-types")
    public ResponseEntity<PetTypeDto> createPetType(@RequestBody PetTypeDto dto) { return ResponseEntity.ok(petTypeService.createPetType(dto)); }

    @PutMapping("/pet-types/{id}")
    public ResponseEntity<PetTypeDto> updatePetType(@PathVariable int id, @RequestBody PetTypeDto dto) { return ResponseEntity.ok(petTypeService.updatePetType(id, dto)); }

    @DeleteMapping("/pet-types/{id}")
    public ResponseEntity<Void> deletePetType(@PathVariable int id) { petTypeService.deletePetType(id); return ResponseEntity.ok().build(); }

    // --- Зміна ролі користувача ---
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.updateUserByAdmin(id, dto));
    }

    // --- АНКЕТИ ---
    @PostMapping("/roommate-requests")
    public ResponseEntity<RoommateRequestDto> createRoommateRequest(@RequestBody AdminRequestSaveDto dto) {
        return ResponseEntity.ok(roommateRequestService.saveRequestByAdmin(null, dto));
    }
    @PutMapping("/roommate-requests/{id}")
    public ResponseEntity<RoommateRequestDto> updateRoommateRequest(@PathVariable String id, @RequestBody AdminRequestSaveDto dto) {
        return ResponseEntity.ok(roommateRequestService.saveRequestByAdmin(id, dto));
    }

}