package com.roomies.backend.services;

import com.roomies.backend.dto.UserReviewCreateDto;
import com.roomies.backend.dto.UserReviewDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.User;
import com.roomies.backend.models.UserReview;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.repositories.UserReviewRepository;
import com.roomies.backend.security.SecurityUtils;

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

    @Autowired private SecurityUtils securityUtils;

    public List<UserReviewDto> getReviewsForUser(UUID targetUserId) {
        return reviewRepository.findByTargetUserIdOrderByCreatedAtDesc(targetUserId)
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Double getUserAverageRating(UUID targetUserId) {
        return reviewRepository.getAverageRatingForUser(targetUserId);
    }

    public UserReviewDto createReview(UserReviewCreateDto dto) {
        // 1. ОДРАЗУ отримуємо поточного автора з токена
        User author = securityUtils.getCurrentUser();

        // 2. Перевірка: Оцінка від 1 до 5
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new ResourceNotFoundException("Оцінка повинна бути від 1 до 5");
        }

        // 3. Чи не оцінює людина сама себе
        if (author.getId().equals(dto.getTargetUserId())) {
            throw new ResourceNotFoundException("Ви не можете залишити відгук самому собі");
        }

        // 4. ТУТ ВИПРАВЛЕНО: Використовуємо author.getId() замість dto.getAuthorId()
        if (reviewRepository.existsByAuthorIdAndTargetUserId(author.getId(), dto.getTargetUserId())) {
            throw new ResourceNotFoundException("Ви вже залишали відгук цьому користувачу. Ви можете його лише оновити.");
        }
        
        User targetUser = userRepository.findById(dto.getTargetUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено"));

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