package com.roomies.backend.dto.filters;

import java.math.BigDecimal;

public class RoommateFilterDto {
    private Integer targetCityId;
    private BigDecimal minBudget;
    private BigDecimal maxBudget;
    private Integer minAge;
    private Integer maxAge;
    private String gender;
    private Boolean isSmoker; // true - курить, false - не курить, null - байдуже
    private Boolean hasPets;  // true - є тварини, false - немає, null - байдуже

    // --- Гетери та Сетери ---
    public Integer getTargetCityId() { return targetCityId; } public void setTargetCityId(Integer targetCityId) { this.targetCityId = targetCityId; }
    public BigDecimal getMinBudget() { return minBudget; } public void setMinBudget(BigDecimal minBudget) { this.minBudget = minBudget; }
    public BigDecimal getMaxBudget() { return maxBudget; } public void setMaxBudget(BigDecimal maxBudget) { this.maxBudget = maxBudget; }
    public Integer getMinAge() { return minAge; } public void setMinAge(Integer minAge) { this.minAge = minAge; }
    public Integer getMaxAge() { return maxAge; } public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }
    public String getGender() { return gender; } public void setGender(String gender) { this.gender = gender; }
    public Boolean getIsSmoker() { return isSmoker; } public void setIsSmoker(Boolean isSmoker) { this.isSmoker = isSmoker; }
    public Boolean getHasPets() { return hasPets; } public void setHasPets(Boolean hasPets) { this.hasPets = hasPets; }
    
}