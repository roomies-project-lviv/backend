/*
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
*/
package com.roomies.backend.controllers;

import com.roomies.backend.dto.*;
import com.roomies.backend.models.RefreshToken;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.security.CustomUserDetailsService;
import com.roomies.backend.security.JwtService;
import com.roomies.backend.security.RefreshTokenService;
import com.roomies.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private JwtService jwtService;
    @Autowired private RefreshTokenService refreshTokenService;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    // РЕЄСТРАЦІЯ (АВТО-ЛОГІН)
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserCreateDto request) {
        // 1. Створюємо юзера (UserService хешує пароль і зберігає)
        userService.createUser(request);
        
        // 2. Дістаємо створеного юзера
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // 3. Генеруємо токени
        String jwtToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new ResponseEntity<>(new AuthResponseDto(jwtToken, refreshToken.getToken()), HttpStatus.CREATED);
    }

    // ЛОГІН
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String jwtToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new AuthResponseDto(jwtToken, refreshToken.getToken()));
    }

    // ОНОВЛЕННЯ ТОКЕНА
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody TokenRefreshRequestDto request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                    String newAccessToken = jwtService.generateToken(userDetails);
                    // Повертаємо новий Access Token і той самий Refresh Token
                    return ResponseEntity.ok(new AuthResponseDto(newAccessToken, request.getRefreshToken()));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token не знайдено"));
    }

    // Вихід з системи (ВИДАЛЕННЯ ТОКЕНА)
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenRefreshRequestDto request) {
        // Шукаємо токен і видаляємо його з бази
        refreshTokenService.findByToken(request.getRefreshToken())
                .ifPresent(token -> userRepository.findById(token.getUser().getId())
                        .ifPresent(user -> refreshTokenService.deleteByUser(user)));
        return ResponseEntity.ok("Успішний вихід з системи");
    }


}