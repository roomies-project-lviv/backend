package com.roomies.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cities") // Таблиця буде називатися cities
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Для Integer зазвичай використовують IDENTITY (або SERIAL у PostgreSQL)
    private Integer id;

    @Column(nullable = false, unique = true) // Назва міста має бути обов'язковою
    private String name;

    // Гетери та сетери
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}