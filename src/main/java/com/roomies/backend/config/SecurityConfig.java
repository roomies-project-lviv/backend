package com.roomies.backend.config;

import com.roomies.backend.security.CustomUserDetailsService;
import com.roomies.backend.security.JwtAuthenticationEntryPoint;
import com.roomies.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;                  // ДОДАНО ІМПОРТ
import org.springframework.web.cors.CorsConfigurationSource;            // ДОДАНО ІМПОРТ
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;     // ДОДАНО ІМПОРТ

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // Буде шукати бін corsConfigurationSource()
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // 1. ВІДКРИТІ ШЛЯХИ
                        // ЗМІНЕНО: Шлях відновлення паролю тепер підпадає під /api/auth/**, 
                        // але про всяк випадок залишаємо обидві варіації
                        .requestMatchers("/api/auth/**", "/api/password-reset/**", "/api/cities").permitAll()
                        .requestMatchers("/ws/**", "/error").permitAll()

                        // 2. ПУБЛІЧНИЙ ПЕРЕГЛЯД
                        .requestMatchers(HttpMethod.GET, "/api/listings/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/listings/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/roommate-requests/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/roommate-requests/search").permitAll()

                        // 3. АДМІНКА
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 4. ВСЕ ІНШЕ
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ДОДАНО БІН ДЛЯ КОРЕКТНОЇ РОБОТИ CORS НА RENDER
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Вказуємо адреси твого локального фронтенду та задеплоєного на Render Static Site
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200", 
                "https://roomies-g1e4.onrender.com"
        ));
        
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Дозволяємо JWT авторизацію
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Застосовуємо до всіх ендпоінтів
        return source;
    }
}