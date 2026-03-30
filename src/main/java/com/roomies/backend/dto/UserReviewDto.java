package com.roomies.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserReviewDto {
    private UUID id;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    
    // Інформація про автора відгуку
    private UUID authorId;
    private String authorFirstName;
    private String authorAvatarUrl;

    // Кому залишили (іноді потрібно для розуміння контексту)
    private UUID targetUserId;

    // --- Гетери та Сетери ---
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public Integer getRating() { return rating; } public void setRating(Integer rating) { this.rating = rating; }
    public String getContent() { return content; } public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public UUID getAuthorId() { return authorId; } public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public String getAuthorFirstName() { return authorFirstName; } public void setAuthorFirstName(String authorFirstName) { this.authorFirstName = authorFirstName; }
    public String getAuthorAvatarUrl() { return authorAvatarUrl; } public void setAuthorAvatarUrl(String authorAvatarUrl) { this.authorAvatarUrl = authorAvatarUrl; }
    public UUID getTargetUserId() { return targetUserId; } public void setTargetUserId(UUID targetUserId) { this.targetUserId = targetUserId; }

    
}