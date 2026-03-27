package com.roomies.backend.repositories;

import com.roomies.backend.models.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Integer> {
    // Spring Data JPA автоматично згенерує базові методи (save, findAll, deleteById)
}