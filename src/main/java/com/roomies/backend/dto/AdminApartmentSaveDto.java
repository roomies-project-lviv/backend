package com.roomies.backend.dto;
import java.util.UUID;

public class AdminApartmentSaveDto {
    private String address;
    private Double area;
    private Integer roomsTotal;
    private int cityId;

    public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
    public Double getArea() { return area; } public void setArea(Double area) { this.area = area; }
    public Integer getRoomsTotal() { return roomsTotal; } public void setRoomsTotal(Integer roomsTotal) { this.roomsTotal = roomsTotal; }
    public int getCityId() { return cityId; } public void setCityId(int cityId) { this.cityId = cityId; }
}