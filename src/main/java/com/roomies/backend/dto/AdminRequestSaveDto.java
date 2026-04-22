package com.roomies.backend.dto;
import java.math.BigDecimal;
import java.util.UUID;

public class AdminRequestSaveDto {
    private String requirements; 
    private BigDecimal budgetMax;
    private UUID authorId;
    private int targetCityId;

    public String getRequirements() { return requirements; } 
    public void setRequirements(String requirements) { this.requirements = requirements; }
    
    public BigDecimal getBudgetMax() { return budgetMax; } 
    public void setBudgetMax(BigDecimal budgetMax) { this.budgetMax = budgetMax; }
    
    public UUID getAuthorId() { return authorId; } 
    public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    
    public int getTargetCityId() { return targetCityId; } 
    public void setTargetCityId(int targetCityId) { this.targetCityId = targetCityId; }
}