package com.roomies.backend.dto;

import com.roomies.backend.models.LifestyleProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String avatarUrl;
    private String occupation;
    private String bio;
    private LocalDateTime createdAt;
    private LifestyleProfile lifestyleProfile;
    private String phoneNumber;

    // --- Гетери та Сетери ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

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
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LifestyleProfile getLifestyleProfile() { return lifestyleProfile; }
    public void setLifestyleProfile(LifestyleProfile lifestyleProfile) { this.lifestyleProfile = lifestyleProfile; }

    private Integer petTypeId;
    private String petTypeName;

    // --- Гетери та Сетери для нових полів ---

    public Integer getPetTypeId() { return petTypeId; }
    public void setPetTypeId(Integer petTypeId) { this.petTypeId = petTypeId; }

    public String getPetTypeName() { return petTypeName; }
    public void setPetTypeName(String petTypeName) { this.petTypeName = petTypeName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

}