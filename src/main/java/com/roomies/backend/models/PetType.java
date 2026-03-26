package com.roomies.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pet_types")
public class PetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY ідеально підходить для типу SERIAL в PostgreSQL
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    // --- Гетери та Сетери ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
}