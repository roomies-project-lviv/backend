package com.roomies.backend.repositories;

import com.roomies.backend.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByNameIgnoreCase(String name);
    // Spring Data JPA сам створить усі базові методи (save, findById, findAll, delete)
}
