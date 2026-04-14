package com.roomies.backend.dto;
import java.time.Instant;
import java.util.UUID;

public class ChatRoomDto {
    private UUID id;
    private UUID otherUserId; // ID співрозмовника
    private String otherUserFirstName; // Ім'я співрозмовника
    private Instant lastMessageAt;

    // Гетери та Сетери
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public UUID getOtherUserId() { return otherUserId; } public void setOtherUserId(UUID otherUserId) { this.otherUserId = otherUserId; }
    public String getOtherUserFirstName() { return otherUserFirstName; } public void setOtherUserFirstName(String otherUserFirstName) { this.otherUserFirstName = otherUserFirstName; }
    public Instant getLastMessageAt() { return lastMessageAt; } public void setLastMessageAt(Instant lastMessageAt) { this.lastMessageAt = lastMessageAt; }

}