package com.roomies.backend.dto;

import java.time.LocalDate;

public class ApartmentCreateDto {
    private Double area;
    private Integer roomsTotal;
    private String address;
    private Double latitude;
    private Double longitude;
    private String description;
    private LocalDate availableFrom;

    // --- Гетери та Сетери ---
    public Double getArea() { return area; } public void setArea(Double area) { this.area = area; }
    public Integer getRoomsTotal() { return roomsTotal; } public void setRoomsTotal(Integer roomsTotal) { this.roomsTotal = roomsTotal; }
    public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
    public Double getLatitude() { return latitude; } public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; } public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public LocalDate getAvailableFrom() { return availableFrom; } public void setAvailableFrom(LocalDate availableFrom) { this.availableFrom = availableFrom; }
    
}