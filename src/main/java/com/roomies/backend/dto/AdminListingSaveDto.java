package com.roomies.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AdminListingSaveDto {
    private String title;
    private BigDecimal pricePerMonth;
    private UUID authorId;
    private UUID apartmentId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public BigDecimal getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }
    public UUID getAuthorId() { return authorId; }
    public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public UUID getApartmentId() { return apartmentId; }
    public void setApartmentId(UUID apartmentId) { this.apartmentId = apartmentId; }
}