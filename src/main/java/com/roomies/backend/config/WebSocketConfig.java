package com.roomies.backend.config;

import com.roomies.backend.security.JwtChannelInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtChannelInterceptor jwtChannelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Ендпоінт, до якого буде підключатися Angular: http://localhost:8080/ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*"); // Дозволяємо підключення з будь-якого порту (фронтенду)
                // .withSockJS(); // Можна увімкнути SockJS для сумісності зі старими браузерами, але сучасний Angular прекрасно працює без нього
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Префікс для повідомлень, які фронтенд ВІДПРАВЛЯЄ на бекенд (напр. /app/chat.send)
        registry.setApplicationDestinationPrefixes("/app");
        
        // Префікси для повідомлень, які бекенд РОЗСИЛАЄ юзерам
        // /user - для приватних повідомлень
        registry.enableSimpleBroker("/user");
        
        // Префікс для визначення конкретного юзера (щоб відправляти приватно)
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Реєструємо наш перехоплювач токенів!
        registration.interceptors(jwtChannelInterceptor);
    }

    
}