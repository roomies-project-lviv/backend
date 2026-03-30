package com.roomies.backend.dto;

import java.util.UUID;

public class UserReviewCreateDto {
    private UUID authorId;
    private UUID targetUserId;
    private Integer rating;
    private String content;

    // --- Гетери та Сетери ---
    public UUID getAuthorId() { return authorId; } public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public UUID getTargetUserId() { return targetUserId; } public void setTargetUserId(UUID targetUserId) { this.targetUserId = targetUserId; }
    public Integer getRating() { return rating; } public void setRating(Integer rating) { this.rating = rating; }
    public String getContent() { return content; } public void setContent(String content) { this.content = content; }

    
}