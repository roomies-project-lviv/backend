package com.roomies.backend.repositories;

import com.roomies.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Spring Boot сам напише SQL-запит для цього методу
    // Він шукатиме користувача за його email
    Optional<User> findByEmail(String email);

}