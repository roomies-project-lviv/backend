package com.roomies.backend.specifications;

import com.roomies.backend.dto.filters.ListingFilterDto;
import com.roomies.backend.models.ApartmentListing;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ApartmentListingSpecification {

    public static Specification<ApartmentListing> withFilter(ListingFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Тільки активні оголошення
            predicates.add(cb.isTrue(root.get("isActive")));

            // Фільтр за містом (точний ID)
            if (filter.getCityId() != null) {
                predicates.add(cb.equal(root.get("city").get("id"), filter.getCityId()));
            }

            // ЦІНА
            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerMonth"), filter.getMinPrice()));
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerMonth"), filter.getMaxPrice()));
            }

            // ПЛОЩА (Додано)
            if (filter.getMinArea() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("area"), filter.getMinArea()));
            }
            if (filter.getMaxArea() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("area"), filter.getMaxArea()));
            }

            // КІЛЬКІСТЬ КІМНАТ (Збережено твій List.in())
            if (filter.getRoomsTotal() != null && !filter.getRoomsTotal().isEmpty()) {
                predicates.add(root.get("roomsTotal").in(filter.getRoomsTotal()));
            }

            // ПОШУК ПО АДРЕСІ (Збережено твою логіку LIKE)
            if (filter.getAddressSearch() != null && !filter.getAddressSearch().trim().isEmpty()) {
                String search = "%" + filter.getAddressSearch().toLowerCase() + "%";
                Predicate addressMatch = cb.like(cb.lower(root.get("address")), search);
                // Також шукаємо в назві міста, якщо раптом cityId не передано
                Predicate cityMatch = cb.like(cb.lower(root.join("city").get("name")), search);
                predicates.add(cb.or(addressMatch, cityMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}