package com.roomies.backend.dto.filters;

import java.math.BigDecimal;

public class RoommateFilterDto {
    private Integer targetCityId;
    private BigDecimal minBudget;
    private BigDecimal maxBudget;
    
    // ЗМІНЕНО: Тепер назви співпадають з Angular
    private Integer ageMin; 
    private Integer ageMax; 
    private String gender;
    
    private Boolean hasPets;  

    // Лайфстайл
    private Boolean isSmoker;
    private Boolean partyHabits;
    private Boolean drinksAlcohol;
    private String sleepSchedule;
    private String noiseTolerance;
    private String guestsFrequency;
    private String cleanlinessLevel;
    private String dietaryPreferences;

    // --- Гетери та Сетери ---
    public Integer getTargetCityId() { return targetCityId; } public void setTargetCityId(Integer targetCityId) { this.targetCityId = targetCityId; }
    public BigDecimal getMinBudget() { return minBudget; } public void setMinBudget(BigDecimal minBudget) { this.minBudget = minBudget; }
    public BigDecimal getMaxBudget() { return maxBudget; } public void setMaxBudget(BigDecimal maxBudget) { this.maxBudget = maxBudget; }
    
    // ЗМІНЕНО: Гетери і сетери для віку
    public Integer getAgeMin() { return ageMin; } public void setAgeMin(Integer ageMin) { this.ageMin = ageMin; }
    public Integer getAgeMax() { return ageMax; } public void setAgeMax(Integer ageMax) { this.ageMax = ageMax; }
    
    public String getGender() { return gender; } public void setGender(String gender) { this.gender = gender; }
    public Boolean getHasPets() { return hasPets; } public void setHasPets(Boolean hasPets) { this.hasPets = hasPets; }

    public Boolean getIsSmoker() { return isSmoker; } public void setIsSmoker(Boolean isSmoker) { this.isSmoker = isSmoker; }
    public Boolean getPartyHabits() { return partyHabits; } public void setPartyHabits(Boolean partyHabits) { this.partyHabits = partyHabits; }
    public Boolean getDrinksAlcohol() { return drinksAlcohol; } public void setDrinksAlcohol(Boolean drinksAlcohol) { this.drinksAlcohol = drinksAlcohol; }
    public String getSleepSchedule() { return sleepSchedule; } public void setSleepSchedule(String sleepSchedule) { this.sleepSchedule = sleepSchedule; }
    public String getNoiseTolerance() { return noiseTolerance; } public void setNoiseTolerance(String noiseTolerance) { this.noiseTolerance = noiseTolerance; }
    public String getGuestsFrequency() { return guestsFrequency; } public void setGuestsFrequency(String guestsFrequency) { this.guestsFrequency = guestsFrequency; }
    public String getCleanlinessLevel() { return cleanlinessLevel; } public void setCleanlinessLevel(String cleanlinessLevel) { this.cleanlinessLevel = cleanlinessLevel; }
    public String getDietaryPreferences() { return dietaryPreferences; } public void setDietaryPreferences(String dietaryPreferences) { this.dietaryPreferences = dietaryPreferences; }
}