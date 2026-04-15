package com.roomies.backend.repositories;

import com.roomies.backend.models.ChatRoom;
import com.roomies.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    
    // Знайти всі діалоги користувача (сортуємо за останнім повідомленням)
    @Query("SELECT c FROM ChatRoom c WHERE c.user1 = :user OR c.user2 = :user ORDER BY c.lastMessageAt DESC")
    List<ChatRoom> findAllByUserOrderByLastMessageAtDesc(@Param("user") User user);

    // Знайти конкретну кімнату між двома користувачами (порядок не важливий)
    @Query("SELECT c FROM ChatRoom c WHERE (c.user1 = :u1 AND c.user2 = :u2) OR (c.user1 = :u2 AND c.user2 = :u1)")
    Optional<ChatRoom> findByUsers(@Param("u1") User u1, @Param("u2") User u2);
}