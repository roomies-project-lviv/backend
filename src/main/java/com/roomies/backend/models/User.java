package com.roomies.backend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Entity // Вказує Spring Boot, що цей клас пов'язаний з базою даних
@Table(name = "users") // Вказуємо точну назву твоєї таблиці
@DynamicInsert
@DynamicUpdate
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

    private String occupation;

    @Column(columnDefinition = "TEXT")
    private String bio; 

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "lifestyle_flags", columnDefinition = "jsonb")
    private LifestyleProfile lifestyleProfile;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserWorkSchedule workSchedule;

    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSocialLink> socialLinks = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApartmentListing> apartmentListings = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    

    // Гетери та Сетери

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LifestyleProfile getLifestyleProfile() { return lifestyleProfile; }
    public void setLifestyleProfile(LifestyleProfile lifestyleProfile) { this.lifestyleProfile = lifestyleProfile; }

    public UserWorkSchedule getWorkSchedule() { return workSchedule; }
    public void setWorkSchedule(UserWorkSchedule workSchedule) { this.workSchedule = workSchedule; }

    public List<UserSocialLink> getSocialLinks() { return socialLinks; }
    public void setSocialLinks(List<UserSocialLink> socialLinks) { this.socialLinks = socialLinks; }

    public List<ApartmentListing> getApartmentListings() { return apartmentListings; }
    public void setApartmentListings(List<ApartmentListing> apartmentListings) { this.apartmentListings = apartmentListings; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
}