package com.roomies.backend.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetInitDto {
    @NotBlank(message = "Email є обов'язковим")
    @Email(message = "Некоректний формат email")
    private String email;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
}