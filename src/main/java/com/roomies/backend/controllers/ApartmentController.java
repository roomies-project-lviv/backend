package com.roomies.backend.controllers;

import com.roomies.backend.dto.ApartmentCreateDto;
import com.roomies.backend.dto.ApartmentDto;
import com.roomies.backend.services.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @GetMapping
    public ResponseEntity<List<ApartmentDto>> getAll() {
        return ResponseEntity.ok(apartmentService.getAllApartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(apartmentService.getApartmentById(id));
    }

    @PostMapping
    public ResponseEntity<ApartmentDto> createApartment(@RequestBody ApartmentCreateDto createDto) {
        return new ResponseEntity<>(apartmentService.createApartment(createDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApartmentDto> updateApartment(@PathVariable UUID id, @RequestBody ApartmentCreateDto updateDto) {
        return ResponseEntity.ok(apartmentService.updateApartment(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable UUID id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }

    
}