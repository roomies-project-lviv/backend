package com.roomies.backend.services;

import com.roomies.backend.dto.FavoriteListingRequestDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.ApartmentListing;
import com.roomies.backend.models.FavoriteApartmentListing;
import com.roomies.backend.models.User;
import com.roomies.backend.repositories.ApartmentListingRepository;
import com.roomies.backend.repositories.FavoriteApartmentListingRepository;
import com.roomies.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteApartmentListingService {

    @Autowired private FavoriteApartmentListingRepository favoriteRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ApartmentListingRepository listingRepository;

    public void addFavorite(FavoriteListingRequestDto requestDto) {
        if (favoriteRepository.existsByUserIdAndListingId(requestDto.getUserId(), requestDto.getListingId())) {
            throw new ResourceNotFoundException("Оголошення вже в обраному");
        }

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено"));
        
        ApartmentListing listing = listingRepository.findById(requestDto.getListingId())
                .orElseThrow(() -> new ResourceNotFoundException("Оголошення не знайдено"));

        FavoriteApartmentListing favorite = new FavoriteApartmentListing();
        favorite.setUser(user);
        favorite.setListing(listing);
        favoriteRepository.save(favorite);
    }

    public void removeFavorite(FavoriteListingRequestDto requestDto) {
        FavoriteApartmentListing favorite = favoriteRepository
                .findByUserIdAndListingId(requestDto.getUserId(), requestDto.getListingId())
                .orElseThrow(() -> new ResourceNotFoundException("Це оголошення не в обраному"));
        
        favoriteRepository.delete(favorite);
    }

    // Повертає список ID обраних оголошень користувача
    public List<UUID> getUserFavoriteListingIds(UUID userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(fav -> fav.getListing().getId())
                .collect(Collectors.toList());
    }

    
}