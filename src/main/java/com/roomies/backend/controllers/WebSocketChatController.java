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
        try {
            String senderEmail = principal.getName();

            ChatMessageDto savedMessage = chatService.saveWebSocketMessage(
                    payload.getRoomId(), 
                    payload.getContent(), 
                    senderEmail
            );

            // ВАЖЛИВО: Відправляємо повідомлення у спільний канал кімнати!
            String destination = "/topic/room/" + payload.getRoomId();
            System.out.println("🔵 [WebSocket] Бродкаст у кімнату: " + destination);

            messagingTemplate.convertAndSend(destination, savedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}