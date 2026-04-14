package com.roomies.backend.controllers;

import com.roomies.backend.dto.ChatMessageDto;
import com.roomies.backend.dto.ChatMessagePayload;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Головний інструмент для розсилки повідомлень

    // @MessageMapping перехоплює повідомлення, які Angular відправляє на "/app/chat.send"
    @MessageMapping("/chat.send")
    public void processMessage(@Payload ChatMessagePayload payload, Principal principal) {
        
        // 1. Отримуємо email відправника з STOMP сесії (з нашого JwtChannelInterceptor)
        String senderEmail = principal.getName();

        // 2. Зберігаємо повідомлення в базу даних через сервіс
        ChatMessageDto savedMessage = chatService.saveWebSocketMessage(
                payload.getRoomId(), 
                payload.getContent(), 
                senderEmail
        );

        // 3. Знаходимо email одержувача, щоб знати, в яку "поштову скриньку" кидати повідомлення
        User recipient = userRepository.findById(payload.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Одержувача не знайдено"));

        // 4. ВІДПРАВЛЯЄМО ПОВІДОМЛЕННЯ!
        // Spring автоматично перетворить це на чергу: /user/{recipientEmail}/queue/messages
        messagingTemplate.convertAndSendToUser(
                recipient.getEmail(), 
                "/queue/messages", 
                savedMessage
        );
    }


}