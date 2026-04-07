package com.roomies.backend.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordResetConfirmDto {
    private String email;
    private String otpCode;
    
    @NotBlank(message = "Пароль є обов'язковим")
    @Size(min = 8, message = "Мінімальна довжина паролю 8 символів")
    private String newPassword;

    // Гетери та сетери
    public String getEmail() { return email; } 
    public void setEmail(String email) { this.email = email; }
    
    public String getOtpCode() { return otpCode; } 
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public String getNewPassword() { return newPassword; } 
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    
}