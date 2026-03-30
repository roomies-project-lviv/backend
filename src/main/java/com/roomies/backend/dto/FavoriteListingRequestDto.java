package com.roomies.backend.dto;

import java.util.UUID;

public class FavoriteListingRequestDto {
    private UUID userId;
    private UUID listingId;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getListingId() { return listingId; }
    public void setListingId(UUID listingId) { this.listingId = listingId; }
    
}