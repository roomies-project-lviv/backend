package com.roomies.backend.dto;

import java.util.UUID;

public class FavoriteListingRequestDto {
    private UUID listingId;

    public UUID getListingId() { return listingId; }
    public void setListingId(UUID listingId) { this.listingId = listingId; }
    
}