package com.roomies.backend.repositories;

import com.roomies.backend.models.ChatMessage;
import com.roomies.backend.models.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    // Отримати всі повідомлення для кімнати, відсортовані від найстарішого до найновішого
    List<ChatMessage> findByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);
}