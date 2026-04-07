package com.roomies.backend.controllers;

import com.roomies.backend.dto.*;
import com.roomies.backend.models.RefreshToken;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.security.CustomUserDetailsService;
import com.roomies.backend.security.JwtService;
import com.roomies.backend.security.RefreshTokenService;
import com.roomies.backend.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.roomies.backend.services.EmailService;
import com.roomies.backend.security.OtpRegistrationService;
import com.roomies.backend.security.OtpPasswordResetService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private JwtService jwtService;
    @Autowired private RefreshTokenService refreshTokenService;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private OtpRegistrationService otpService;
    @Autowired private OtpPasswordResetService otpPasswordResetService;

    // Ініціалізація реєстрації (перевіряємо дані, генеруємо код, шлемо лист)
    @PostMapping("/register/init")
    public ResponseEntity<String> initRegistration(@Valid @RequestBody UserCreateDto request) {
        // Перевіряємо, чи немає вже такого юзера в базі
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Користувач з таким email вже існує");
        }

        // Зберігаємо в кеш і отримуємо код
        String otp = otpService.generateAndStoreOtp(request);
        
        // Відправляємо на пошту (в окремому потоці, щоб не блокувати запит)
        new Thread(() -> emailService.sendOtpEmail(request.getEmail(), otp)).start();

        return ResponseEntity.ok("Код підтвердження відправлено на пошту");
    }

    // Верифікація коду (якщо ок - зберігаємо в БД і даємо токени)
    @PostMapping("/register/verify")
    public ResponseEntity<AuthResponseDto> verifyAndRegister(
            @RequestParam String email, 
            @RequestParam String otpCode, 
            HttpServletResponse response) {
        
        // Перевіряємо код (якщо помилка - вилетить Exception)
        UserCreateDto validUserDto = otpService.verifyOtp(email, otpCode);

        // Далі йде стандартна реєстрація з твого старого коду
        userService.createUser(validUserDto);
        User user = userRepository.findByEmail(validUserDto.getEmail()).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String jwtToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        setRefreshTokenCookie(response, refreshToken.getToken());

        return new ResponseEntity<>(new AuthResponseDto(jwtToken), HttpStatus.CREATED);
    }

    // Повторна відправка коду (Resend)
    @PostMapping("/register/resend")
    public ResponseEntity<String> resendOtp(@RequestBody UserCreateDto request) {
        String newOtp = otpService.generateAndStoreOtp(request);
        new Thread(() -> emailService.sendOtpEmail(request.getEmail(), newOtp)).start();
        return ResponseEntity.ok("Новий код відправлено");
    }

    // Утиліта для додавання HttpOnly куки
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // Фронтенд (JS) не матиме доступу до цієї куки
        cookie.setSecure(false); // ВАЖЛИВО: На продакшені (HTTPS) має бути true! На localhost - false.
        cookie.setPath("/api/auth"); // Ця кука буде відправлятися ТІЛЬКИ на ендпоінти авторизації
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 днів у секундах
        response.addCookie(cookie);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserCreateDto request, HttpServletResponse response) {
        userService.createUser(request);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String jwtToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        setRefreshTokenCookie(response, refreshToken.getToken());

        return new ResponseEntity<>(new AuthResponseDto(jwtToken), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String jwtToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        setRefreshTokenCookie(response, refreshToken.getToken());

        return ResponseEntity.ok(new AuthResponseDto(jwtToken));
    }

    // ЧИТАЄМО ТОКЕН З КУКИ
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken, 
            HttpServletResponse response) {
        
        if (refreshToken == null) {
            throw new RuntimeException("Refresh token відсутній");
        }

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                    String newAccessToken = jwtService.generateToken(userDetails);
                    return ResponseEntity.ok(new AuthResponseDto(newAccessToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token не знайдено"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            refreshTokenService.findByToken(refreshToken)
                .ifPresent(token -> userRepository.findById(token.getUser().getId())
                    .ifPresent(user -> refreshTokenService.deleteByUser(user)));
        }

        // Очищаємо куку
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/api/auth");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Успішний вихід з системи");
    }

    
    @PostMapping("/password-reset/init")
    public ResponseEntity<String> initPasswordReset(@Valid @RequestBody PasswordResetInitDto request) {
        // Перевіряємо чи є такий юзер взагалі
        if (!userRepository.existsByEmail(request.getEmail())) {
            // За правилами безпеки ми завжди кажемо "Якщо email існує, ми відправили код",
            // щоб хакери не могли "промацувати" базу на існуючі емейли.
            return ResponseEntity.ok("Якщо такий email зареєстрований, ми надіслали на нього код.");
        }

        String otp = otpPasswordResetService.generateAndStoreOtp(request.getEmail());
        new Thread(() -> emailService.sendPasswordResetOtp(request.getEmail(), otp)).start();

        return ResponseEntity.ok("Код надіслано");
    }

    @PostMapping("/password-reset/verify")
    public ResponseEntity<String> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmDto request) {
        // 1. Перевіряємо код
        otpPasswordResetService.verifyOtp(request.getEmail(), request.getOtpCode());
        
        // 2. Оновлюємо пароль
        userService.updatePassword(request.getEmail(), request.getNewPassword());
        
        return ResponseEntity.ok("Пароль успішно змінено");
    }

    
}