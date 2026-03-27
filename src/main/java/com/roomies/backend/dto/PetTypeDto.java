package com.roomies.backend.dto;

public class PetTypeDto {
    
    private Integer id;
    private String name;

    public PetTypeDto() {}

    public PetTypeDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // --- Гетери та Сетери ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
}