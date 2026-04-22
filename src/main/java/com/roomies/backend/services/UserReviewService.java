package com.roomies.backend.services;

import com.roomies.backend.dto.UserReviewCreateDto;
import com.roomies.backend.dto.UserReviewDto;
import com.roomies.backend.dto.UserReviewUpdateDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.exceptions.UnauthorizedAccessException;
import com.roomies.backend.models.User;
import com.roomies.backend.models.UserReview;
import com.roomies.backend.repositories.UserRepository;
import com.roomies.backend.repositories.UserReviewRepository;
import com.roomies.backend.security.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserReviewService {

    @Autowired private UserReviewRepository reviewRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SecurityUtils securityUtils;

    // ОНОВЛЕНО: Тепер повертає Page замість List
    public Page<UserReviewDto> getReviewsForUser(UUID targetUserId, Pageable pageable) {
        return reviewRepository.findByTargetUserIdOrderByCreatedAtDesc(targetUserId, pageable)
                .map(this::convertToDto);
    }

    public Double getUserAverageRating(UUID targetUserId) {
        return reviewRepository.getAverageRatingForUser(targetUserId);
    }

    public Page<UserReviewDto> getMyReviews(Pageable pageable) {
        User me = securityUtils.getCurrentUser();
        return getReviewsForUser(me.getId(), pageable);
    }

    public Double getMyAverageRating() {
        User me = securityUtils.getCurrentUser();
        return getUserAverageRating(me.getId());
    }
    public UserReviewDto createReview(UserReviewCreateDto dto) {
        User author = securityUtils.getCurrentUser();

        // Перевірку на "від 1 до 5" видалено, бо тепер це робить @Valid у контролері

        if (author.getId().equals(dto.getTargetUserId())) {
            throw new IllegalArgumentException("Ви не можете залишити відгук самому собі");
        }

        if (reviewRepository.existsByAuthorIdAndTargetUserId(author.getId(), dto.getTargetUserId())) {
            throw new IllegalArgumentException("Ви вже залишали відгук цьому користувачу. Ви можете його лише оновити.");
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

    // ДОДАНО: Метод оновлення відгуку
    public UserReviewDto updateReview(UUID reviewId, UserReviewUpdateDto dto) {
        User author = securityUtils.getCurrentUser();

        UserReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Відгук не знайдено"));

        // БЕЗПЕКА: Перевіряємо, чи є поточний користувач автором цього відгуку
        if (!review.getAuthor().getId().equals(author.getId())) {
            throw new UnauthorizedAccessException("Ви не можете редагувати чужий відгук");
        }

        review.setRating(dto.getRating());
        review.setContent(dto.getContent());

        UserReview updated = reviewRepository.save(review);
        return convertToDto(updated);
    }

    public void deleteReview(UUID id) {
        User author = securityUtils.getCurrentUser();

        UserReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Відгук не знайдено"));

        // БЕЗПЕКА: Перевіряємо, чи є поточний користувач автором цього відгуку
        if (!review.getAuthor().getId().equals(author.getId())) {
            throw new UnauthorizedAccessException("Ви не можете видалити чужий відгук");
        }

        reviewRepository.delete(review);
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