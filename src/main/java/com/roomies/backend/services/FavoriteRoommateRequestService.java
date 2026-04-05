package com.roomies.backend.services;

import com.roomies.backend.dto.FavoriteRoommateRequestDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.FavoriteRoommateRequest;
import com.roomies.backend.models.RoommateRequest;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.FavoriteRoommateRequestRepository;
import com.roomies.backend.repositories.RoommateRequestRepository;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.security.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FavoriteRoommateRequestService {

    @Autowired
    private FavoriteRoommateRequestRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoommateRequestRepository roommateRequestRepository;

    @Autowired private SecurityUtils securityUtils;

    // Logic 1: Add to favorites
    @Transactional
    public FavoriteRoommateRequestDto addFavorite(UUID userId, UUID requestId) {
        User user = securityUtils.getCurrentUser();

        // Перевіряємо, чи існує запит на сусіда
        RoommateRequest request = roommateRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("RoommateRequest not found with id: " + requestId));

        // Перевіряємо на дублікат (чи вже лайкнуто)
        if (favoriteRepository.existsByUserIdAndRequestId(userId, requestId)) {
            throw new ResourceNotFoundException("This request is already in favorites for user: " + userId);
        }

        FavoriteRoommateRequest favorite = new FavoriteRoommateRequest();
        favorite.setUser(user);
        favorite.setRequest(request);

        FavoriteRoommateRequest savedFavorite = favoriteRepository.save(favorite);
        return convertToDto(savedFavorite);
    }

    // Logic 2: Remove from favorites
    @Transactional
    public void removeFavorite(UUID userId, UUID requestId) {
        if (!favoriteRepository.existsByUserIdAndRequestId(userId, requestId)) {
            throw new ResourceNotFoundException("Favorite not found to remove");
        }
        favoriteRepository.deleteByUserIdAndRequestId(userId, requestId);
    }

    // Logic 3: Get all favorite requests for a specific user
    public List<FavoriteRoommateRequestDto> getUserFavorites(UUID userId) {
        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // --- Допоміжний метод ---
    private FavoriteRoommateRequestDto convertToDto(FavoriteRoommateRequest favorite) {
        FavoriteRoommateRequestDto dto = new FavoriteRoommateRequestDto();
        dto.setId(favorite.getId());
        dto.setUserId(favorite.getUser().getId());
        dto.setRequestId(favorite.getRequest().getId());
        dto.setCreatedAt(favorite.getCreatedAt());
        return dto;
    }
}