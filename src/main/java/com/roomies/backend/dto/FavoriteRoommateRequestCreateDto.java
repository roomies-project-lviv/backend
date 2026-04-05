package com.roomies.backend.dto;

import java.util.UUID;

public class FavoriteRoommateRequestCreateDto {
    private UUID requestId;

    public UUID getRequestId() { return requestId; }
    public void setRequestId(UUID requestId) { this.requestId = requestId; }
}