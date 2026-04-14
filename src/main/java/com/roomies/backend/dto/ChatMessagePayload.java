package com.roomies.backend.dto;

import java.util.UUID;

public class ChatMessagePayload {
    private UUID roomId;
    private UUID recipientId; // Кому відправляємо
    private String content;

    // Гетери та Сетери
    public UUID getRoomId() { return roomId; } public void setRoomId(UUID roomId) { this.roomId = roomId; }
    public UUID getRecipientId() { return recipientId; } public void setRecipientId(UUID recipientId) { this.recipientId = recipientId; }
    public String getContent() { return content; } public void setContent(String content) { this.content = content; }
    
}