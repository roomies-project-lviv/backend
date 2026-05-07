package com.roomies.backend.dto.filters;

import java.math.BigDecimal;
import java.util.List;

public class ListingFilterDto {
    private Long cityId; // Для точного вибору з випадаючого списку
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Integer> roomsTotal; // Наприклад: [1, 2]
    private Double minArea;
    private Double maxArea;
    private String addressSearch; // Пошук по вулиці/району
    private Double radiusLat;
    private Double radiusLng;
    private Double radiusKm;

    private Boolean wifi;
    private Boolean washingMachine;
    private Boolean boiler;
    private Boolean airConditioner;
    private Boolean dishwasher;
    private Boolean elevator;
    private Boolean shelter;
    private Boolean parking;
    private Boolean security;
    private Boolean petFriendly;
    private Boolean kidsFriendly;

    // --- Гетери та Сетери ---
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public List<Integer> getRoomsTotal() { return roomsTotal; }
    public void setRoomsTotal(List<Integer> roomsTotal) { this.roomsTotal = roomsTotal; }

    public Double getMinArea() { return minArea; }
    public void setMinArea(Double minArea) { this.minArea = minArea; }

    public Double getMaxArea() { return maxArea; }
    public void setMaxArea(Double maxArea) { this.maxArea = maxArea; }

    public String getAddressSearch() { return addressSearch; }
    public void setAddressSearch(String addressSearch) { this.addressSearch = addressSearch; }

    public Double getRadiusLat() { return radiusLat; } 
    public void setRadiusLat(Double radiusLat) { this.radiusLat = radiusLat; }
    
    public Double getRadiusLng() { return radiusLng; } 
    public void setRadiusLng(Double radiusLng) { this.radiusLng = radiusLng; }
    
    public Double getRadiusKm() { return radiusKm; } 
    public void setRadiusKm(Double radiusKm) { this.radiusKm = radiusKm; }

    public Boolean getWifi() { return wifi; } public void setWifi(Boolean wifi) { this.wifi = wifi; }
    public Boolean getWashingMachine() { return washingMachine; } public void setWashingMachine(Boolean washingMachine) { this.washingMachine = washingMachine; }
    public Boolean getBoiler() { return boiler; } public void setBoiler(Boolean boiler) { this.boiler = boiler; }
    public Boolean getAirConditioner() { return airConditioner; } public void setAirConditioner(Boolean airConditioner) { this.airConditioner = airConditioner; }
    public Boolean getDishwasher() { return dishwasher; } public void setDishwasher(Boolean dishwasher) { this.dishwasher = dishwasher; }
    public Boolean getElevator() { return elevator; } public void setElevator(Boolean elevator) { this.elevator = elevator; }
    public Boolean getShelter() { return shelter; } public void setShelter(Boolean shelter) { this.shelter = shelter; }
    public Boolean getParking() { return parking; } public void setParking(Boolean parking) { this.parking = parking; }
    public Boolean getSecurity() { return security; } public void setSecurity(Boolean security) { this.security = security; }
    public Boolean getPetFriendly() { return petFriendly; } public void setPetFriendly(Boolean petFriendly) { this.petFriendly = petFriendly; }
    public Boolean getKidsFriendly() { return kidsFriendly; } public void setKidsFriendly(Boolean kidsFriendly) { this.kidsFriendly = kidsFriendly; }

}