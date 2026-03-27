package com.roomies.backend.repositories;

import com.roomies.backend.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    // Spring Data JPA сам створить усі базові методи (save, findById, findAll, delete)
}