package com.roomies.backend.security;

import com.roomies.backend.exceptions.InvalidTokenException;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new InvalidTokenException("Користувач не авторизований");
        }

        // В authentication.getName() лежить email, який ми поклали туди у JwtAuthenticationFilter
        String email = authentication.getName(); 
        
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new InvalidTokenException("Користувача з токена не знайдено в базі"));
    }

    
}