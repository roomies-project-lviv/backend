package com.roomies.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("danieldragomanov@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Код підтвердження реєстрації ROOMIES");
        message.setText("Ваш код підтвердження: " + otpCode + "\n\nКод дійсний 5 хвилин.");
        
        mailSender.send(message);
    }
    
    public void sendPasswordResetOtp(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("danieldragomanov@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Відновлення паролю ROOMIES");
        message.setText("Ваш код для відновлення паролю: " + otpCode + "\n\nЯкщо ви не робили цей запит, проігноруйте цей лист.\nКод дійсний 5 хвилин.");
                
        mailSender.send(message);
    }
    
}