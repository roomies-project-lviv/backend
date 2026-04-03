package com.roomies.backend.services;

import com.roomies.backend.dto.UserWorkScheduleDto;
import com.roomies.backend.models.User;
import com.roomies.backend.models.UserWorkSchedule;
import com.roomies.backend.repositories.UserWorkScheduleRepository;
import com.roomies.backend.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserWorkScheduleService {

    @Autowired private UserWorkScheduleRepository scheduleRepository;
    @Autowired private SecurityUtils securityUtils; // <---

    public UserWorkScheduleDto getMySchedule() {
        User me = securityUtils.getCurrentUser();
        return scheduleRepository.findById(me.getId())
                .map(this::convertToDto)
                .orElse(null);
    }

    @Transactional
    public UserWorkScheduleDto updateOrCreateMySchedule(UserWorkScheduleDto dto) {
        User me = securityUtils.getCurrentUser();

        UserWorkSchedule schedule = scheduleRepository.findById(me.getId())
                .orElse(new UserWorkSchedule());

        schedule.setUser(me);
        me.setWorkSchedule(schedule);

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

    @Transactional
    public void deleteMySchedule() {
        User me = securityUtils.getCurrentUser();
        UserWorkSchedule schedule = scheduleRepository.findById(me.getId()).orElse(null);

        if (schedule == null) return;

        me.setWorkSchedule(null);
        scheduleRepository.delete(schedule);
    }

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