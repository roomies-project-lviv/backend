package com.roomies.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Порожньо. CORS тепер керується ТІЛЬКИ через SecurityConfig.java
}