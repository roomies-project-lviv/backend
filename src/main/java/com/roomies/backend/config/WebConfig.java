package com.roomies.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Дозволяємо CORS для всіх наших ендпоінтів
                .allowedOrigins(
                    "http://localhost:4200", 
                    "https://frontend-3t7r.onrender.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Дозволені HTTP методи
                .allowedHeaders("*") // Дозволяємо будь-які заголовки
                .allowCredentials(true); // Дозволяємо передачу кукі та авторизаційних даних (для JWT)
    }

}