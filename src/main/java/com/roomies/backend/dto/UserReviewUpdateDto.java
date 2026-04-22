package com.roomies.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserReviewUpdateDto {

    @NotNull(message = "Оцінка є обов'язковою")
    @Min(value = 1, message = "Мінімальна оцінка - 1")
    @Max(value = 5, message = "Максимальна оцінка - 5")
    private Integer rating;

    @NotBlank(message = "Текст відгуку не може бути порожнім")
    @Size(max = 1000, message = "Максимальна довжина відгуку - 1000 символів")
    private String content;

    // --- Гетери та Сетери ---
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}