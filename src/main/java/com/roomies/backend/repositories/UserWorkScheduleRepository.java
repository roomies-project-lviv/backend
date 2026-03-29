package com.roomies.backend.repositories;

import com.roomies.backend.models.UserWorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserWorkScheduleRepository extends JpaRepository<UserWorkSchedule, UUID> {
}