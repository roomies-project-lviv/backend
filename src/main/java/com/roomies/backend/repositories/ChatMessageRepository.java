package com.roomies.backend.repositories;

import com.roomies.backend.models.ChatMessage;
import com.roomies.backend.models.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.roomies.backend.models.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    // Отримати всі повідомлення для кімнати, відсортовані від найстарішого до найновішого
    List<ChatMessage> findByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);

    // Рахує, скільки повідомлень у цій кімнаті НЕ від мене і ще не прочитані
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.chatRoom = :room AND m.sender != :user AND m.isRead = false")
    int countUnreadMessages(@Param("room") ChatRoom room, @Param("user") User user);

    // Масово ставить статус isRead = true для всіх чужих повідомлень у цій кімнаті
    @Modifying
    @Query("UPDATE ChatMessage m SET m.isRead = true WHERE m.chatRoom = :room AND m.sender != :user AND m.isRead = false")
    void markAsReadByRoomAndUser(@Param("room") ChatRoom room, @Param("user") User user);

    // Рахує загальну кількість непрочитаних повідомлень для користувача по всіх чат кімнатах
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE (m.chatRoom.user1 = :user OR m.chatRoom.user2 = :user) AND m.sender != :user AND m.isRead = false")
    int countTotalUnreadMessages(@Param("user") User user);
}