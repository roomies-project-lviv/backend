package com.roomies.backend.security;

import com.roomies.backend.models.User;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Шукаємо нашого юзера в базі
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача з email " + email + " не знайдено"));

        // Перетворюємо нашого User на стандартного UserDetails, який розуміє Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>() // Тут мають бути ролі (USER, ADMIN), поки залишаємо порожнім
        );
    }

    
}