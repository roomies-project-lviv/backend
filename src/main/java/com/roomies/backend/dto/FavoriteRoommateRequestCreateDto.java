package com.roomies.backend.dto;

import java.util.UUID;

public class FavoriteRoommateRequestCreateDto {
    private UUID userId;
    private UUID requestId;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getRequestId() { return requestId; }
    public void setRequestId(UUID requestId) { this.requestId = requestId; }
}