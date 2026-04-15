package com.roomies.backend.models;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "last_message_at", nullable = false)
    private Instant lastMessageAt = Instant.now();

    // Гетери та Сетери
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public User getUser1() { return user1; } public void setUser1(User user1) { this.user1 = user1; }
    public User getUser2() { return user2; } public void setUser2(User user2) { this.user2 = user2; }
    public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getLastMessageAt() { return lastMessageAt; } public void setLastMessageAt(Instant lastMessageAt) { this.lastMessageAt = lastMessageAt; }

}