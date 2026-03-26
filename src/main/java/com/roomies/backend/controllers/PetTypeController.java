package com.roomies.backend.controllers;

import com.roomies.backend.dto.PetTypeDto;
import com.roomies.backend.services.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pet-types")
public class PetTypeController {

    @Autowired
    private PetTypeService petTypeService;

    @GetMapping
    public ResponseEntity<List<PetTypeDto>> getAllPetTypes() {
        return ResponseEntity.ok(petTypeService.getAllPetTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetTypeDto> getPetTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(petTypeService.getPetTypeById(id));
    }

    @PostMapping
    public ResponseEntity<PetTypeDto> createPetType(@RequestBody PetTypeDto dto) {
        return new ResponseEntity<>(petTypeService.createPetType(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetTypeDto> updatePetType(@PathVariable Integer id, @RequestBody PetTypeDto dto) {
        return ResponseEntity.ok(petTypeService.updatePetType(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetType(@PathVariable Integer id) {
        petTypeService.deletePetType(id);
        return ResponseEntity.noContent().build();
    }

    
}