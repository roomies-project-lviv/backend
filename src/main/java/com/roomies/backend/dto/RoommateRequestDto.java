package com.roomies.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class RoommateRequestDto {
    private UUID id;
    private BigDecimal budgetMax;
    private LocalDate moveInDate;
    private String requirements;
    private Boolean isActive;
    private LocalDateTime createdAt;

    // Дані про автора
    private UUID authorId;
    private String authorFirstName;
    private String authorAvatarUrl;
    private String authorOccupation;
    private LocalDate authorBirthDate;
    
    // Дані про бажане місто
    private Integer targetCityId;
    private String targetCityName;

    private Integer matchPercentage;

    // --- Гетери та Сетери ---
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public BigDecimal getBudgetMax() { return budgetMax; } public void setBudgetMax(BigDecimal budgetMax) { this.budgetMax = budgetMax; }
    public LocalDate getMoveInDate() { return moveInDate; } public void setMoveInDate(LocalDate moveInDate) { this.moveInDate = moveInDate; }
    public String getRequirements() { return requirements; } public void setRequirements(String requirements) { this.requirements = requirements; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public UUID getAuthorId() { return authorId; } public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public String getAuthorFirstName() { return authorFirstName; } public void setAuthorFirstName(String authorFirstName) { this.authorFirstName = authorFirstName; }
    public String getAuthorAvatarUrl() { return authorAvatarUrl; } public void setAuthorAvatarUrl(String authorAvatarUrl) { this.authorAvatarUrl = authorAvatarUrl; }
    public String getAuthorOccupation() { return authorOccupation; } public void setAuthorOccupation(String authorOccupation) { this.authorOccupation = authorOccupation; }
    public LocalDate getAuthorBirthDate() { return authorBirthDate; } public void setAuthorBirthDate(LocalDate authorBirthDate) { this.authorBirthDate = authorBirthDate; }

    public Integer getTargetCityId() { return targetCityId; } public void setTargetCityId(Integer targetCityId) { this.targetCityId = targetCityId; }
    public String getTargetCityName() { return targetCityName; } public void setTargetCityName(String targetCityName) { this.targetCityName = targetCityName; }

    public Integer getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(Integer matchPercentage) { this.matchPercentage = matchPercentage; }

}