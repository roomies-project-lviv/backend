package com.roomies.backend.dto;

import java.util.List;
import java.util.UUID;

public class UserWorkScheduleDto {
    private UUID userId;
    private String scheduleType;
    private String baseLocation;
    private String evenPeriodLocation;
    private String oddPeriodLocation;
    private List<Integer> week1OfficeDays;
    private List<Integer> week2OfficeDays;
    private Integer shiftWorkDays;
    private Integer shiftRestDays;

    // --- Гетери та Сетери ---

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getScheduleType() { return scheduleType; }
    public void setScheduleType(String scheduleType) { this.scheduleType = scheduleType; }

    public String getBaseLocation() { return baseLocation; }
    public void setBaseLocation(String baseLocation) { this.baseLocation = baseLocation; }

    public String getEvenPeriodLocation() { return evenPeriodLocation; }
    public void setEvenPeriodLocation(String evenPeriodLocation) { this.evenPeriodLocation = evenPeriodLocation; }

    public String getOddPeriodLocation() { return oddPeriodLocation; }
    public void setOddPeriodLocation(String oddPeriodLocation) { this.oddPeriodLocation = oddPeriodLocation; }

    public List<Integer> getWeek1OfficeDays() { return week1OfficeDays; }
    public void setWeek1OfficeDays(List<Integer> week1OfficeDays) { this.week1OfficeDays = week1OfficeDays; }

    public List<Integer> getWeek2OfficeDays() { return week2OfficeDays; }
    public void setWeek2OfficeDays(List<Integer> week2OfficeDays) { this.week2OfficeDays = week2OfficeDays; }

    public Integer getShiftWorkDays() { return shiftWorkDays; }
    public void setShiftWorkDays(Integer shiftWorkDays) { this.shiftWorkDays = shiftWorkDays; }

    public Integer getShiftRestDays() { return shiftRestDays; }
    public void setShiftRestDays(Integer shiftRestDays) { this.shiftRestDays = shiftRestDays; }
}