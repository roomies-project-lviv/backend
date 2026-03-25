package com.roomies.backend.controllers;

import com.roomies.backend.models.User;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController 
@RequestMapping("/api/users") // Це базова адреса. Всі запити сюди починатимуться з /api/users
public class UserController {

    @Autowired // Автоматично підключаємо наш репозиторій, щоб ми могли працювати з базою даних
    private UserRepository userRepository;

    // Цей метод спрацює, коли хтось перейде за адресою /api/users
    @GetMapping
    public List<User> getAllUsers() {
        // Просимо репозиторій знайти ВСІХ користувачів у базі і повертаємо їх
        return userRepository.findAll();
    }
}