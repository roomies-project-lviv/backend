package com.roomies.backend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_work_schedules")
public class UserWorkSchedule {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    // Зв'язок 1-до-1 з User. @MapsId каже, що id цього класу - це id юзера
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "schedule_type")
    private String scheduleType;

    @Column(name = "base_location")
    private String baseLocation;

    @Column(name = "even_period_location")
    private String evenPeriodLocation;

    @Column(name = "odd_period_location")
    private String oddPeriodLocation;

    // Магія Hibernate 6 для масивів PostgreSQL (_int4)
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "week1_office_days", columnDefinition = "int4[]")
    private List<Integer> week1OfficeDays;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "week2_office_days", columnDefinition = "int4[]")
    private List<Integer> week2OfficeDays;

    @Column(name = "shift_work_days")
    private Integer shiftWorkDays;

    @Column(name = "shift_rest_days")
    private Integer shiftRestDays;

    // --- Гетери та Сетери ---

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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