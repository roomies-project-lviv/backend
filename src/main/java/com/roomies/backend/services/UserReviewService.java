package com.roomies.backend.services;

import com.roomies.backend.dto.UserReviewCreateDto;
import com.roomies.backend.dto.UserReviewDto;
import com.roomies.backend.models.User;
import com.roomies.backend.models.UserReview;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.repositories.UserReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserReviewService {

    @Autowired private UserReviewRepository reviewRepository;
    @Autowired private UserRepository userRepository;

    public List<UserReviewDto> getReviewsForUser(UUID targetUserId) {
        return reviewRepository.findByTargetUserIdOrderByCreatedAtDesc(targetUserId)
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Double getUserAverageRating(UUID targetUserId) {
        return reviewRepository.getAverageRatingForUser(targetUserId);
    }

    public UserReviewDto createReview(UserReviewCreateDto dto) {
        // Перевірка 1: Чи не оцінює людина сама себе
        if (dto.getAuthorId().equals(dto.getTargetUserId())) {
            throw new RuntimeException("Ви не можете залишити відгук самому собі");
        }

        // Перевірка 2: Оцінка від 1 до 5
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new RuntimeException("Оцінка повинна бути від 1 до 5");
        }

        // Перевірка 3: Чи вже був відгук
        if (reviewRepository.existsByAuthorIdAndTargetUserId(dto.getAuthorId(), dto.getTargetUserId())) {
            throw new RuntimeException("Ви вже залишали відгук цьому користувачу. Ви можете його лише оновити.");
        }

        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Автора не знайдено"));
        
        User targetUser = userRepository.findById(dto.getTargetUserId())
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        UserReview review = new UserReview();
        review.setAuthor(author);
        review.setTargetUser(targetUser);
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());

        UserReview saved = reviewRepository.save(review);
        return convertToDto(saved);
    }

    public void deleteReview(UUID id) {
        reviewRepository.deleteById(id);
    }

    // --- Перекладач ---
    private UserReviewDto convertToDto(UserReview entity) {
        UserReviewDto dto = new UserReviewDto();
        dto.setId(entity.getId());
        dto.setRating(entity.getRating());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt());
        
        if (entity.getAuthor() != null) {
            dto.setAuthorId(entity.getAuthor().getId());
            dto.setAuthorFirstName(entity.getAuthor().getFirstName());
            dto.setAuthorAvatarUrl(entity.getAuthor().getAvatarUrl());
        }
        
        if (entity.getTargetUser() != null) {
            dto.setTargetUserId(entity.getTargetUser().getId());
        }
        
        return dto;
    }

    
}