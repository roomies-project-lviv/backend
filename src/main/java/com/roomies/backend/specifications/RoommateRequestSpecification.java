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
            Join<RoommateRequest, User> author = root.join("author");

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

            // ЗМІНЕНО: Вік рахується правильно
            if (filter.getAgeMin() != null) {
                LocalDate maxBirthDate = LocalDate.now().minusYears(filter.getAgeMin());
                predicates.add(cb.lessThanOrEqualTo(author.get("birthDate"), maxBirthDate));
            }
            if (filter.getAgeMax() != null) {
                LocalDate minBirthDate = LocalDate.now().minusYears(filter.getAgeMax() + 1);
                predicates.add(cb.greaterThan(author.get("birthDate"), minBirthDate));
            }

            // ЗМІНЕНО: Безпечне порівняння статі (переводимо Enum у текст і порівнюємо)
            if (filter.getGender() != null && !filter.getGender().isEmpty()) {
                predicates.add(cb.equal(
                    cb.upper(author.get("gender").as(String.class)), 
                    filter.getGender().toUpperCase()
                ));
            }

            if (filter.getHasPets() != null) {
                if (filter.getHasPets()) {
                    predicates.add(cb.isNotNull(author.get("petType")));
                } else {
                    predicates.add(cb.isNull(author.get("petType")));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}