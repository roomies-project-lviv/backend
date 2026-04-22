package com.roomies.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ApartmentListingCreateDto {
    private String title;
    private BigDecimal pricePerMonth;
    private String address;
    private Double area;
    private int roomsTotal;
    private int cityId;

    // --- Гетери та Сетери ---
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public BigDecimal getPricePerMonth() { return pricePerMonth; } public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }
    public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
    public Double getArea() { return area; } public void setArea(Double area) { this.area = area; }
    public int getRoomsTotal() { return roomsTotal; } public void setRoomsTotal(int roomsTotal) { this.roomsTotal = roomsTotal; }
    public int getCityId() { return cityId; } public void setCityId(int cityId) { this.cityId = cityId; }
    
}