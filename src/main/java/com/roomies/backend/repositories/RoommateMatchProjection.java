package com.roomies.backend.repositories;

import com.roomies.backend.models.RoommateRequest;

// Spring Data автоматично реалізує цей інтерфейс під капотом
public interface RoommateMatchProjection {
    RoommateRequest getRequest();
    Integer getMatchPercentage();
}