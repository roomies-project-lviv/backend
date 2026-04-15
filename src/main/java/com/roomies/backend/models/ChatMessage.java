package com.roomies.backend.models;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Instant timestamp = Instant.now();

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    // Гетери та Сетери
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public ChatRoom getChatRoom() { return chatRoom; } public void setChatRoom(ChatRoom chatRoom) { this.chatRoom = chatRoom; }
    public User getSender() { return sender; } public void setSender(User sender) { this.sender = sender; }
    public String getContent() { return content; } public void setContent(String content) { this.content = content; }
    public Instant getTimestamp() { return timestamp; } public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public Boolean getIsRead() { return isRead; } public void setIsRead(Boolean isRead) { this.isRead = isRead; }

}