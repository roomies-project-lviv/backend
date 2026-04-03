package com.roomies.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ApartmentListingCreateDto {
    private UUID apartmentId;
    private String title;
    private BigDecimal pricePerMonth;

    // --- Гетери та Сетери ---
    public UUID getApartmentId() { return apartmentId; } public void setApartmentId(UUID apartmentId) { this.apartmentId = apartmentId; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public BigDecimal getPricePerMonth() { return pricePerMonth; } public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }

    
}