package com.roomies.backend.config;

import com.roomies.backend.security.CustomUserDetailsService;
import com.roomies.backend.security.JwtAuthenticationFilter; // <--- ДОДАЙ ІМПОРТ
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter; // <--- ІН'ЄКЦІЯ НАШОГО ФІЛЬТРА

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
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Дозволяємо логін без токена
                .requestMatchers("/api/auth/**").permitAll() 
                // ТАКОЖ дозволяємо реєстрацію юзера без токена
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users").permitAll() 
                
                // Опціонально: можна дозволити всім дивитися оголошення (GET)
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/listings/**").permitAll()
                
                // ВСІ ІНШІ запити (створення квартири, відгуку, тощо) вимагають токен!
                .anyRequest().authenticated() 
            )
            // ДОДАЄМО НАШ ФІЛЬТР ПЕРЕД СТАНДАРТНИМ ФІЛЬТРОМ SPRING
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    
}