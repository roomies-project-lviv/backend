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

            // 1. Зберігаємо повідомлення (сервіс сам розуміє, хто відправник)
            ChatMessageDto savedMessage = chatService.saveWebSocketMessage(
                    payload.getRoomId(), 
                    payload.getContent(), 
                    senderEmail
            );

            // 2. Відправляємо у відкриту радіо-кімнату (для тих, хто зараз сидить у цьому чаті)
            String roomDestination = "/topic/room/" + payload.getRoomId();
            messagingTemplate.convertAndSend(roomDestination, savedMessage);

            // 3. ПОВЕРНУЛИ ЦЕЙ РЯДОК: Знаходимо одержувача в базі даних, щоб дізнатися його email
            User recipient = userRepository.findById(payload.getRecipientId())
                    .orElseThrow(() -> new RuntimeException("Одержувача не знайдено"));

            // 4. Глобальне сповіщення на особистий канал одержувача (для червоних кружечків)
            String globalDestination = "/queue/notifications-" + recipient.getEmail();
            messagingTemplate.convertAndSend(globalDestination, savedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}