package com.roomies.backend.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "roommate_requests")
public class RoommateRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Зв'язок з автором анкети (користувачем)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    // Зв'язок з містом, де людина шукає житло
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_city_id")
    private City targetCity;

    @Column(name = "budget_max")
    private BigDecimal budgetMax;

    @Column(name = "move_in_date")
    private LocalDate moveInDate;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Гетери та Сетери ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public City getTargetCity() { return targetCity; }
    public void setTargetCity(City targetCity) { this.targetCity = targetCity; }

    public BigDecimal getBudgetMax() { return budgetMax; }
    public void setBudgetMax(BigDecimal budgetMax) { this.budgetMax = budgetMax; }

    public LocalDate getMoveInDate() { return moveInDate; }
    public void setMoveInDate(LocalDate moveInDate) { this.moveInDate = moveInDate; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    
}