package com.roomies.backend.controllers;

import com.roomies.backend.dto.ChatMessageDto;
import com.roomies.backend.dto.ChatRoomDto;
import com.roomies.backend.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // Отримати всі мої діалоги
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDto>> getMyRooms() {
        return ResponseEntity.ok(chatService.getMyChatRooms());
    }

    // Почати чат з користувачем (або отримати існуючий)
    @PostMapping("/rooms/user/{userId}")
    public ResponseEntity<ChatRoomDto> getOrCreateRoom(@PathVariable UUID userId) {
        return ResponseEntity.ok(chatService.getOrCreateRoom(userId));
    }

    // Отримати історію конкретної кімнати
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getHistory(@PathVariable UUID roomId) {
        return ResponseEntity.ok(chatService.getChatHistory(roomId));
    }

    // Тимчасовий метод для відправки повідомлення через звичайний HTTP (POST)
    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ChatMessageDto> sendMessage(
            @PathVariable UUID roomId, 
            @RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(chatService.saveMessage(roomId, payload.get("content")));
    }

    
    // Позначити всі повідомлення в кімнаті як прочитані
    @PutMapping("/rooms/{roomId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID roomId) {
        chatService.markRoomMessagesAsRead(roomId);
        return ResponseEntity.ok().build();
    }

    // Отримати загальну кількість непрочитаних повідомлень по всіх кімнатах
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Integer>> getTotalUnreadCount() {
        return ResponseEntity.ok(Map.of("count", chatService.getTotalUnreadCount()));
    }

}