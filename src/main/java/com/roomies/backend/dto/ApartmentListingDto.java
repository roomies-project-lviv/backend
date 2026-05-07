package com.roomies.backend.dto;

import com.roomies.backend.models.ApartmentAmenities;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ApartmentListingDto {
    private UUID id;
    private String title;
    private BigDecimal pricePerMonth;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private int cityId;
    private String cityName; 

    // Дані про автора
    private UUID authorId;
    private String authorFirstName;
    private String authorAvatarUrl;

    // Дані про квартиру
    private UUID apartmentId;
    private String address;
    private Double area;
    private int roomsTotal;
    private String apartmentType;
    private ApartmentAmenities amenities;

    // Географічні координати
    private Double latitude;
    private Double longitude;

    // Дані про галерею
    private java.util.List<String> imageUrls;



    // --- Гетери та Сетери ---
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public BigDecimal getPricePerMonth() { return pricePerMonth; } public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public int getCityId() { return cityId; } public void setCityId(int cityId) { this.cityId = cityId; }
    public String getCityName() { return cityName; } public void setCityName(String cityName) { this.cityName = cityName; }

    public UUID getAuthorId() { return authorId; } public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public String getAuthorFirstName() { return authorFirstName; } public void setAuthorFirstName(String authorFirstName) { this.authorFirstName = authorFirstName; }
    public String getAuthorAvatarUrl() { return authorAvatarUrl; } public void setAuthorAvatarUrl(String authorAvatarUrl) { this.authorAvatarUrl = authorAvatarUrl; }
    
    public UUID getApartmentId() { return apartmentId; } public void setApartmentId(UUID apartmentId) { this.apartmentId = apartmentId; }
    public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
    public Double getArea() { return area; } public void setArea(Double area) { this.area = area; }
    public int getRoomsTotal() { return roomsTotal; } public void setRoomsTotal(int roomsTotal) { this.roomsTotal = roomsTotal; }
    public String getApartmentType() { return apartmentType; } public void setApartmentType(String apartmentType) { this.apartmentType = apartmentType; }

    public Double getLatitude() { return latitude; } public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; } public void setLongitude(Double longitude) { this.longitude = longitude; }

    public ApartmentAmenities getAmenities() { return amenities; }
    public void setAmenities(ApartmentAmenities amenities) { this.amenities = amenities; }

    public java.util.List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(java.util.List<String> imageUrls) { this.imageUrls = imageUrls; }

    public String getCoverImageUrl() {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            return imageUrls.get(0);
        }
        return null;
    }

}