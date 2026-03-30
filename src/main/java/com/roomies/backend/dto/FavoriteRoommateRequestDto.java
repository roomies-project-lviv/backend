package com.roomies.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class FavoriteRoommateRequestDto {
    private UUID id;
    private UUID userId;
    private UUID requestId;
    private LocalDateTime createdAt;

    // Гетери та Сетери
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getRequestId() { return requestId; }
    public void setRequestId(UUID requestId) { this.requestId = requestId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}