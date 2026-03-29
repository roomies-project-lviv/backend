package com.roomies.backend.services;

import com.roomies.backend.dto.UserWorkScheduleDto;
import com.roomies.backend.exceptions.UserNotFoundException;
import com.roomies.backend.models.User;
import com.roomies.backend.models.UserWorkSchedule;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.repositories.UserWorkScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserWorkScheduleService {

    @Autowired
    private UserWorkScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    // Отримати розклад
    public UserWorkScheduleDto getSchedule(UUID userId) {
        UserWorkSchedule schedule = scheduleRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Work schedule not found for user: " + userId));
        return convertToDto(schedule);
    }

    // Створити або Оновити розклад (PUT логіка)
    public UserWorkScheduleDto updateOrCreateSchedule(UUID userId, UserWorkScheduleDto dto) {
        // Перевіряємо, чи існує взагалі такий юзер (бо розклад не може існувати без юзера)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Шукаємо існуючий розклад, якщо немає - створюємо новий порожній
        UserWorkSchedule schedule = scheduleRepository.findById(userId)
                .orElse(new UserWorkSchedule());

        // Оновлюємо дані
        schedule.setUser(user); // Прив'язуємо до юзера
        schedule.setUserId(userId);
        schedule.setScheduleType(dto.getScheduleType());
        schedule.setBaseLocation(dto.getBaseLocation());
        schedule.setEvenPeriodLocation(dto.getEvenPeriodLocation());
        schedule.setOddPeriodLocation(dto.getOddPeriodLocation());
        schedule.setWeek1OfficeDays(dto.getWeek1OfficeDays());
        schedule.setWeek2OfficeDays(dto.getWeek2OfficeDays());
        schedule.setShiftWorkDays(dto.getShiftWorkDays());
        schedule.setShiftRestDays(dto.getShiftRestDays());

        UserWorkSchedule savedSchedule = scheduleRepository.save(schedule);
        return convertToDto(savedSchedule);
    }

    // Видалити розклад
    public void deleteSchedule(UUID userId) {
        if (!scheduleRepository.existsById(userId)) {
            throw new RuntimeException("Work schedule not found for user: " + userId);
        }
        scheduleRepository.deleteById(userId);
    }

    // --- Допоміжний метод мапінгу ---
    private UserWorkScheduleDto convertToDto(UserWorkSchedule schedule) {
        UserWorkScheduleDto dto = new UserWorkScheduleDto();
        dto.setUserId(schedule.getUserId());
        dto.setScheduleType(schedule.getScheduleType());
        dto.setBaseLocation(schedule.getBaseLocation());
        dto.setEvenPeriodLocation(schedule.getEvenPeriodLocation());
        dto.setOddPeriodLocation(schedule.getOddPeriodLocation());
        dto.setWeek1OfficeDays(schedule.getWeek1OfficeDays());
        dto.setWeek2OfficeDays(schedule.getWeek2OfficeDays());
        dto.setShiftWorkDays(schedule.getShiftWorkDays());
        dto.setShiftRestDays(schedule.getShiftRestDays());
        return dto;
    }
}