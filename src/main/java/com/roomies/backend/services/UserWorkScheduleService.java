package com.roomies.backend.services;

import com.roomies.backend.dto.UserWorkScheduleDto;
import com.roomies.backend.exceptions.UserNotFoundException;
import com.roomies.backend.models.User;
import com.roomies.backend.models.UserWorkSchedule;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.repositories.UserWorkScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserWorkScheduleService {

    @Autowired
    private UserWorkScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    // Отримати розклад
    public UserWorkScheduleDto getSchedule(UUID userId) {
        return scheduleRepository.findById(userId)
                .map(this::convertToDto) // Якщо знайшли - перетворюємо в DTO
                .orElse(null);           // Якщо немає - просто повертаємо null, ніяких помилок!
    }

    // Створити або Оновити розклад (PUT логіка)
    @Transactional
    public UserWorkScheduleDto updateOrCreateSchedule(UUID userId, UserWorkScheduleDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        UserWorkSchedule schedule = scheduleRepository.findById(userId)
                .orElse(new UserWorkSchedule());

        // Правильно зв'язуємо об'єкти з обох боків
        schedule.setUser(user);
        user.setWorkSchedule(schedule);

        // УВАГА: Ми більше не пишемо schedule.setUserId(userId);
        // @MapsId автоматично витягне його з об'єкта user!

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
    @Transactional
    public void deleteSchedule(UUID userId) {
        UserWorkSchedule schedule = scheduleRepository.findById(userId).orElse(null);

        // Якщо розкладу і так немає, ми просто перериваємо метод (немає що видаляти)
        if (schedule == null) {
            return;
        }

        User user = schedule.getUser();
        if (user != null) {
            user.setWorkSchedule(null);
        }
        scheduleRepository.delete(schedule);
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