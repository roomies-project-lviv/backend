package com.roomies.backend.services;

import com.roomies.backend.dto.PetTypeDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.PetType;
import com.roomies.backend.repositories.PetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetTypeService {

    @Autowired
    private PetTypeRepository petTypeRepository;

    public List<PetTypeDto> getAllPetTypes() {
        return petTypeRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PetTypeDto getPetTypeById(Integer id) {
        PetType petType = petTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet Type not found with ID: " + id));
        return convertToDto(petType);
    }

    public PetTypeDto createPetType(PetTypeDto dto) {
        PetType petType = new PetType();
        petType.setName(dto.getName());
        
        PetType saved = petTypeRepository.save(petType);
        return convertToDto(saved);
    }

    public PetTypeDto updatePetType(Integer id, PetTypeDto dto) {
        PetType existing = petTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet Type not found with ID: " + id));
        
        existing.setName(dto.getName());
        PetType updated = petTypeRepository.save(existing);
        return convertToDto(updated);
    }

    public void deletePetType(Integer id) {
        petTypeRepository.deleteById(id);
    }

    // --- Перекладач (Mapper) ---
    private PetTypeDto convertToDto(PetType entity) {
        return new PetTypeDto(entity.getId(), entity.getName());
    }
    
}