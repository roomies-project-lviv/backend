package com.roomies.backend.controllers;

import com.roomies.backend.dto.AuthRequestDto;
import com.roomies.backend.dto.AuthResponseDto;
import com.roomies.backend.security.CustomUserDetailsService;
import com.roomies.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        // 1. Spring Security сам перевіряє чи збігається пароль з хешем у базі
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Якщо пароль правильний, дістаємо юзера
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // 3. Генеруємо JWT токен
        String jwtToken = jwtService.generateToken(userDetails);

        // 4. Повертаємо токен на фронтенд
        return ResponseEntity.ok(new AuthResponseDto(jwtToken));
    }

    
}