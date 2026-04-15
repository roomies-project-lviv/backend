package com.roomies.backend.services;

import com.roomies.backend.dto.ChatMessageDto;
import com.roomies.backend.dto.ChatRoomDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.exceptions.UnauthorizedAccessException;
import com.roomies.backend.models.ChatMessage;
import com.roomies.backend.models.ChatRoom;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.ChatMessageRepository;
import com.roomies.backend.repositories.ChatRoomRepository;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {

    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SecurityUtils securityUtils;

    // 1. Отримати всі діалоги поточного користувача
    public List<ChatRoomDto> getMyChatRooms() {
        User me = securityUtils.getCurrentUser();
        List<ChatRoom> rooms = chatRoomRepository.findAllByUserOrderByLastMessageAtDesc(me);
        
        return rooms.stream().map(room -> {
            ChatRoomDto dto = new ChatRoomDto();
            dto.setId(room.getId());
            dto.setLastMessageAt(room.getLastMessageAt());
            
            // Визначаємо, хто є "іншим" користувачем у кімнаті
            User otherUser = room.getUser1().getId().equals(me.getId()) ? room.getUser2() : room.getUser1();
            dto.setOtherUserId(otherUser.getId());
            dto.setOtherUserFirstName(otherUser.getFirstName());
            
            return dto;
        }).collect(Collectors.toList());
    }

    // 2. Створити нову кімнату (або повернути існуючу)
    public ChatRoomDto getOrCreateRoom(UUID targetUserId) {
        User me = securityUtils.getCurrentUser();
        if (me.getId().equals(targetUserId)) {
            throw new RuntimeException("Ви не можете створити чат самі з собою");
        }
        
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено"));

        ChatRoom room = chatRoomRepository.findByUsers(me, targetUser).orElseGet(() -> {
            ChatRoom newRoom = new ChatRoom();
            newRoom.setUser1(me);
            newRoom.setUser2(targetUser);
            return chatRoomRepository.save(newRoom);
        });

        ChatRoomDto dto = new ChatRoomDto();
        dto.setId(room.getId());
        dto.setOtherUserId(targetUser.getId());
        dto.setOtherUserFirstName(targetUser.getFirstName());
        dto.setLastMessageAt(room.getLastMessageAt());
        return dto;
    }

    // 3. Отримати історію повідомлень кімнати (з перевіркою безпеки)
    public List<ChatMessageDto> getChatHistory(UUID roomId) {
        User me = securityUtils.getCurrentUser();
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Кімнату не знайдено"));

        // ПЕРЕВІРКА: Чи має поточний юзер доступ до цього чату?
        if (!room.getUser1().getId().equals(me.getId()) && !room.getUser2().getId().equals(me.getId())) {
            throw new UnauthorizedAccessException("Ви не маєте доступу до цього чату");
        }

        return chatMessageRepository.findByChatRoomOrderByTimestampAsc(room)
                .stream().map(msg -> {
                    ChatMessageDto dto = new ChatMessageDto();
                    dto.setId(msg.getId());
                    dto.setChatRoomId(room.getId()); // <--- ДОДАНО ЦЕЙ РЯДОК!
                    dto.setSenderId(msg.getSender().getId());
                    dto.setContent(msg.getContent());
                    dto.setTimestamp(msg.getTimestamp());
                    dto.setIsRead(msg.getIsRead());
                    return dto;
                }).collect(Collectors.toList());
    }

    // 4. Зберегти нове повідомлення
    public ChatMessageDto saveMessage(UUID roomId, String content) {
        User me = securityUtils.getCurrentUser();
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Кімнату не знайдено"));

        if (!room.getUser1().getId().equals(me.getId()) && !room.getUser2().getId().equals(me.getId())) {
            throw new UnauthorizedAccessException("Ви не маєте доступу до цього чату");
        }

        ChatMessage message = new ChatMessage();
        message.setChatRoom(room);
        message.setSender(me);
        message.setContent(content);
        ChatMessage saved = chatMessageRepository.save(message);

        // Оновлюємо час останнього повідомлення в кімнаті (щоб вона піднялася в списку)
        room.setLastMessageAt(saved.getTimestamp());
        chatRoomRepository.save(room);

        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(saved.getId());
        dto.setSenderId(saved.getSender().getId());
        dto.setContent(saved.getContent());
        dto.setTimestamp(saved.getTimestamp());
        dto.setIsRead(saved.getIsRead());
        return dto;
    }


    // Метод для WebSockets
    @Transactional
    public ChatMessageDto saveWebSocketMessage(UUID roomId, String content, String senderEmail) {
        User me = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено"));
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Кімнату не знайдено"));

        if (!room.getUser1().getId().equals(me.getId()) && !room.getUser2().getId().equals(me.getId())) {
            throw new UnauthorizedAccessException("Ви не маєте доступу до цього чату");
        }

        ChatMessage message = new ChatMessage();
        message.setChatRoom(room);
        message.setSender(me);
        message.setContent(content);
        ChatMessage saved = chatMessageRepository.save(message);

        room.setLastMessageAt(saved.getTimestamp());
        chatRoomRepository.save(room);

        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(saved.getId());
        dto.setChatRoomId(room.getId()); // <--- ДОДАНО ЦЕЙ РЯДОК!
        dto.setSenderId(saved.getSender().getId());
        dto.setContent(saved.getContent());
        dto.setTimestamp(saved.getTimestamp());
        dto.setIsRead(saved.getIsRead());
        return dto;
    }


}