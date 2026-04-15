package com.roomies.backend.dto;
import java.time.Instant;
import java.util.UUID;

public class ChatMessageDto {
    private UUID id;
    private UUID chatRoomId;
    private UUID senderId;
    private String content;
    private Instant timestamp;
    private Boolean isRead;

    // Гетери та Сетери...
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public UUID getChatRoomId() { return chatRoomId; } public void setChatRoomId(UUID chatRoomId) { this.chatRoomId = chatRoomId; }
    public UUID getSenderId() { return senderId; } public void setSenderId(UUID senderId) { this.senderId = senderId; }
    public String getContent() { return content; } public void setContent(String content) { this.content = content; }
    public Instant getTimestamp() { return timestamp; } public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public Boolean getIsRead() { return isRead; } public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    
}