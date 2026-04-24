package com.roomies.backend.models;

import java.io.Serializable;

public class LifestyleProfile implements Serializable {
    private String sleepSchedule;
    private String guestsFrequency;
    private String noiseTolerance;
    private String cleanlinessLevel;
    private String dietaryPreferences;

    // Поля, які вже були в JSON (з UserDto)
    private Boolean isSmoker;
    private Boolean drinksAlcohol;
    private Boolean partyHabits;
    private String pet;

    // Геттери та Сеттери
    public String getSleepSchedule() { return sleepSchedule; }
    public void setSleepSchedule(String sleepSchedule) { this.sleepSchedule = sleepSchedule; }

    public String getGuestsFrequency() { return guestsFrequency; }
    public void setGuestsFrequency(String guestsFrequency) { this.guestsFrequency = guestsFrequency; }

    public String getNoiseTolerance() { return noiseTolerance; }
    public void setNoiseTolerance(String noiseTolerance) { this.noiseTolerance = noiseTolerance; }

    public String getCleanlinessLevel() { return cleanlinessLevel; }
    public void setCleanlinessLevel(String cleanlinessLevel) { this.cleanlinessLevel = cleanlinessLevel; }

    public String getDietaryPreferences() { return dietaryPreferences; }
    public void setDietaryPreferences(String dietaryPreferences) { this.dietaryPreferences = dietaryPreferences; }

    public Boolean getIsSmoker() { return isSmoker; }
    public void setIsSmoker(Boolean isSmoker) { this.isSmoker = isSmoker; }

    public Boolean getDrinksAlcohol() { return drinksAlcohol; }
    public void setDrinksAlcohol(Boolean drinksAlcohol) { this.drinksAlcohol = drinksAlcohol; }

    public Boolean getPartyHabits() { return partyHabits; }
    public void setPartyHabits(Boolean partyHabits) { this.partyHabits = partyHabits; }

    public String getPet() { return pet; }
    public void setPet(String pet) { this.pet = pet; }

}