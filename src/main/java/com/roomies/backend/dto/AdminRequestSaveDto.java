package com.roomies.backend.dto;
import java.math.BigDecimal;
import java.util.UUID;

public class AdminRequestSaveDto {
    private String aboutMe;
    private BigDecimal budgetMax;
    private UUID authorId;
    private int targetCityId;

    public String getAboutMe() { return aboutMe; } public void setAboutMe(String aboutMe) { this.aboutMe = aboutMe; }
    public BigDecimal getBudgetMax() { return budgetMax; } public void setBudgetMax(BigDecimal budgetMax) { this.budgetMax = budgetMax; }
    public UUID getAuthorId() { return authorId; } public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public int getTargetCityId() { return targetCityId; } public void setTargetCityId(int targetCityId) { this.targetCityId = targetCityId; }
}