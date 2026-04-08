package com.roomies.backend.dto.filters;

import java.math.BigDecimal;
import java.util.List;

public class ListingFilterDto {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Integer> roomsTotal; // Наприклад: [1, 2] (одно- або двокімнатні)
    private String addressSearch; // Для пошуку по району/вулиці

    // --- Гетери та Сетери ---
    public BigDecimal getMinPrice() { return minPrice; } public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    public BigDecimal getMaxPrice() { return maxPrice; } public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    public List<Integer> getRoomsTotal() { return roomsTotal; } public void setRoomsTotal(List<Integer> roomsTotal) { this.roomsTotal = roomsTotal; }
    public String getAddressSearch() { return addressSearch; } public void setAddressSearch(String addressSearch) { this.addressSearch = addressSearch; }
    
}