package com.roomies.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ApartmentListingDto {
    private UUID id;
    private String title;
    private BigDecimal pricePerMonth;
    private Boolean isActive;
    private LocalDateTime createdAt;

    // Дані про автора
    private UUID authorId;
    private String authorFirstName;
    private String authorAvatarUrl;

    // Дані про квартиру
    private UUID apartmentId;
    private String address;
    private Double area;
    private Integer roomsTotal;

    // --- Гетери та Сетери ---
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public BigDecimal getPricePerMonth() { return pricePerMonth; } public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public UUID getAuthorId() { return authorId; } public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public String getAuthorFirstName() { return authorFirstName; } public void setAuthorFirstName(String authorFirstName) { this.authorFirstName = authorFirstName; }
    public String getAuthorAvatarUrl() { return authorAvatarUrl; } public void setAuthorAvatarUrl(String authorAvatarUrl) { this.authorAvatarUrl = authorAvatarUrl; }
    
    public UUID getApartmentId() { return apartmentId; } public void setApartmentId(UUID apartmentId) { this.apartmentId = apartmentId; }
    public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
    public Double getArea() { return area; } public void setArea(Double area) { this.area = area; }
    public Integer getRoomsTotal() { return roomsTotal; } public void setRoomsTotal(Integer roomsTotal) { this.roomsTotal = roomsTotal; }

    
}