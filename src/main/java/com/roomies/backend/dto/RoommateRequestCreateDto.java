package com.roomies.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RoommateRequestCreateDto {
    private UUID authorId;
    private Integer targetCityId;
    private BigDecimal budgetMax;
    private LocalDate moveInDate;
    private String requirements;

    // --- Гетери та Сетери ---
    public UUID getAuthorId() { return authorId; } public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public Integer getTargetCityId() { return targetCityId; } public void setTargetCityId(Integer targetCityId) { this.targetCityId = targetCityId; }
    public BigDecimal getBudgetMax() { return budgetMax; } public void setBudgetMax(BigDecimal budgetMax) { this.budgetMax = budgetMax; }
    public LocalDate getMoveInDate() { return moveInDate; } public void setMoveInDate(LocalDate moveInDate) { this.moveInDate = moveInDate; }
    public String getRequirements() { return requirements; } public void setRequirements(String requirements) { this.requirements = requirements; }

    
}