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
}