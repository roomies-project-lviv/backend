package com.roomies.backend.dto;

public class CityDto {
    private Integer id;
    private String name;

    // Порожній конструктор
    public CityDto() {}

    // Конструктор з параметрами (знадобиться нам у сервісі)
    public CityDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Гетери та сетери
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}