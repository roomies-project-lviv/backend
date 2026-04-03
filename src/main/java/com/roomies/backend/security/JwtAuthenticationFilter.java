package com.roomies.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // 1. Отримуємо заголовок Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Якщо заголовка немає, або він не починається з "Bearer " - пропускаємо запит далі (він буде відхилений Spring Security)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Відрізаємо слово "Bearer " (7 символів), щоб отримати чистий токен
        jwt = authHeader.substring(7);
        
        // Блок try-catch
        try {
            // 4. Дістаємо email з токена
            userEmail = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Якщо токен недійсний, прострочений або пошкоджений - пропускаємо запит далі без авторизації
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Якщо email є, і користувач ще не авторизований у поточному контексті
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Перевіряємо, чи токен валідний (не прострочений і належить цьому юзеру)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                
                // 7. Створюємо об'єкт авторизації для Spring
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 8. Зберігаємо статус "авторизований" у контекст Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 9. Передаємо запит далі
        filterChain.doFilter(request, response);
    }

    
}