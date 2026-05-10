package com.roomies.backend.dto;
import java.time.Instant;
import java.util.UUID;

public class ChatRoomDto {
    private UUID id;
    private UUID otherUserId; // ID співрозмовника
    private String otherUserFirstName; // Ім'я співрозмовника
    private Instant lastMessageAt;
    private Integer unreadCount;

    private String otherUserAvatarUrl;
    private String lastMessageContent;

    private UUID lastMessageSenderId;
    private Boolean isLastMessageRead;

    // Гетери та Сетери
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public UUID getOtherUserId() { return otherUserId; } public void setOtherUserId(UUID otherUserId) { this.otherUserId = otherUserId; }
    public String getOtherUserFirstName() { return otherUserFirstName; } public void setOtherUserFirstName(String otherUserFirstName) { this.otherUserFirstName = otherUserFirstName; }
    public Instant getLastMessageAt() { return lastMessageAt; } public void setLastMessageAt(Instant lastMessageAt) { this.lastMessageAt = lastMessageAt; }
    public Integer getUnreadCount() { return unreadCount; } public void setUnreadCount(Integer unreadCount) { this.unreadCount = unreadCount; }
    public String getOtherUserAvatarUrl() { return otherUserAvatarUrl; } 
    public void setOtherUserAvatarUrl(String otherUserAvatarUrl) { this.otherUserAvatarUrl = otherUserAvatarUrl; }
    public String getLastMessageContent() { return lastMessageContent; } 
    public void setLastMessageContent(String lastMessageContent) { this.lastMessageContent = lastMessageContent; }
    public UUID getLastMessageSenderId() { return lastMessageSenderId; }
    public void setLastMessageSenderId(UUID lastMessageSenderId) { this.lastMessageSenderId = lastMessageSenderId; }
    public Boolean getIsLastMessageRead() { return isLastMessageRead; }
    public void setIsLastMessageRead(Boolean isLastMessageRead) { this.isLastMessageRead = isLastMessageRead; }
}