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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // <--- ДОДАЙ ІМПОРТ

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
        // Передаємо userDetailsService прямо в конструктор
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
            .cors(Customizer.withDefaults())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                //.requestMatchers("/api/**").permitAll() // Дозволяємо всі запити до /api/ без аутентифікації, але всередині контролерів ми будемо перевіряти токен та ролі
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/listings/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/listings/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/roommates/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/roommates/search").permitAll()
                .requestMatchers("/ws/**").permitAll()  // Дозволяємо ініціювати WebSocket з'єднання всім, але всередині ми перевіримо токен через JwtChannelInterceptor
                .requestMatchers("/api/auth/**", "/api/password-reset/**", "/ws/**", "/api/cities").permitAll()

                // Дозволяємо внутрішні помилки та OPTIONS запити від Angular
                .requestMatchers("/error").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            // ДОДАЄМО НАШ ФІЛЬТР ПЕРЕД СТАНДАРТНИМ ФІЛЬТРОМ SPRING
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    
}