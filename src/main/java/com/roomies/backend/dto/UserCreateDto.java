package com.roomies.backend.dto;

import java.time.LocalDate;

public class UserCreateDto {
    private String email;
    private String password; // Пароль отримуємо, але назад не повернемо
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private Integer petTypeId;


    // --- Гетери та Сетери ---
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
    
    public Integer getPetTypeId() { return petTypeId; }
    public void setPetTypeId(Integer petTypeId) { this.petTypeId = petTypeId; }

}