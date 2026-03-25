package com.roomies.backend.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity // Вказує Spring Boot, що цей клас пов'язаний з базою даних
@Table(name = "users") // Вказуємо точну назву твоєї таблиці 
public class User {

    @Id // Це первинний ключ 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true) // Пошта унікальна і не може бути порожньою [cite: 208]
    private String email;

    @Column(nullable = false)
    private String password; 

    @Column(name = "first_name", nullable = false) // Якщо назва в БД зі зміїним регістром, вказуємо її так 
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName; 

    @Column(name = "birth_date")
    private LocalDate birthDate; 

    private String gender; 
    
    @Column(name = "avatar_url")
    private String avatarUrl; 

    @Column(name = "sleep_schedule")
    private String sleepSchedule;

    private String occupation; 
    
    @Column(name = "guests_frequency")
    private String guestsFrequency; 

    @Column(name = "noise_tolerance")
    private String noiseTolerance; 

    @Column(name = "cleanliness_level")
    private String cleanlinessLevel; 

    @Column(name = "dietary_preferences")
    private String dietaryPreferences; 

    @Column(columnDefinition = "TEXT")
    private String bio; 

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt; 

    @Column(name = "lifestyle_flags", columnDefinition = "bit(8)")
    private String lifestyleFlags;

    // (Поки що я пропустив pet_type_id та lifestyle_flags, ми додамо їх наступним кроком, коли налаштуємо зв'язки з іншими таблицями)

    // Гетери та Сетери

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getLifestyleFlags() { return lifestyleFlags; }
    public void setLifestyleFlags(String lifestyleFlags) { this.lifestyleFlags = lifestyleFlags; }

}