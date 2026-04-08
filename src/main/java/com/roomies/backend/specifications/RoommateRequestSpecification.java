package com.roomies.backend.specifications;

import com.roomies.backend.dto.filters.RoommateFilterDto;
import com.roomies.backend.models.RoommateRequest;
import com.roomies.backend.models.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoommateRequestSpecification {

    public static Specification<RoommateRequest> withFilter(RoommateFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // З'єднуємо таблицю анкет із таблицею авторів (користувачів)
            Join<RoommateRequest, User> author = root.join("author");

            // Завжди показуємо лише активні анкети
            predicates.add(cb.isTrue(root.get("isActive")));

            if (filter.getTargetCityId() != null) {
                predicates.add(cb.equal(root.get("targetCity").get("id"), filter.getTargetCityId()));
            }

            if (filter.getMinBudget() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("budgetMax"), filter.getMinBudget()));
            }
            if (filter.getMaxBudget() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("budgetMax"), filter.getMaxBudget()));
            }

            // Розрахунок віку по даті народження
            if (filter.getMinAge() != null) {
                LocalDate maxBirthDate = LocalDate.now().minusYears(filter.getMinAge());
                predicates.add(cb.lessThanOrEqualTo(author.get("birthDate"), maxBirthDate));
            }
            if (filter.getMaxAge() != null) {
                LocalDate minBirthDate = LocalDate.now().minusYears(filter.getMaxAge() + 1);
                predicates.add(cb.greaterThan(author.get("birthDate"), minBirthDate));
            }

            if (filter.getGender() != null && !filter.getGender().isEmpty()) {
                predicates.add(cb.equal(author.get("gender"), filter.getGender()));
            }

            // Фільтр по тваринах
            if (filter.getHasPets() != null) {
                if (filter.getHasPets()) {
                    predicates.add(cb.isNotNull(author.get("petType")));
                } else {
                    predicates.add(cb.isNull(author.get("petType")));
                }
            }

            // Фільтр по курінню (нульовий біт у lifestyleFlags)
            if (filter.getIsSmoker() != null) {
                String smokerFlag = filter.getIsSmoker() ? "1%" : "0%";
                predicates.add(cb.like(author.get("lifestyleFlags"), smokerFlag));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    
}