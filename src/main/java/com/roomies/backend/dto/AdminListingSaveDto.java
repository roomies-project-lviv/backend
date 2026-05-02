package com.roomies.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AdminListingSaveDto {
    private String title;
    private BigDecimal pricePerMonth;
    private UUID authorId;
    private String address;
    private Double area;
    private Integer roomsTotal;
    private int cityId;
    private String apartmentType;

    public String getTitle() { return title; }  public void setTitle(String title) { this.title = title; }
    public BigDecimal getPricePerMonth() { return pricePerMonth; }  public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }
    public UUID getAuthorId() { return authorId; }  public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
    public Double getArea() { return area; } public void setArea(Double area) { this.area = area; }
    public Integer getRoomsTotal() { return roomsTotal; } public void setRoomsTotal(Integer roomsTotal) { this.roomsTotal = roomsTotal; }
    public int getCityId() { return cityId; } public void setCityId(int cityId) { this.cityId = cityId; }
    public String getApartmentType() { return apartmentType; } public void setApartmentType(String apartmentType) { this.apartmentType = apartmentType; }
    
}