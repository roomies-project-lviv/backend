package com.roomies.backend.models;

import java.io.Serializable;

public class ApartmentAmenities implements Serializable {
    private Boolean wifi = false;
    private Boolean washingMachine = false;
    private Boolean boiler = false;
    private Boolean airConditioner  = false;
    private Boolean dishwasher = false;
    private Boolean elevator = false;
    private Boolean shelter = false;
    private Boolean parking = false;
    private Boolean security = false;
    private Boolean petFriendly = false;
    private Boolean kidsFriendly =  false;

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
