package com.roomies.backend.repositories;

import java.util.UUID;

public interface RoommateMatchProjection {
    UUID getRequestId(); // Беремо тільки ID анкети
    Integer getMatchPercentage(); // Беремо порахований бал
}