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

            predicates.add(cb.isTrue(root.get("isActive")));

            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerMonth"), filter.getMinPrice()));
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerMonth"), filter.getMaxPrice()));
            }

            // КІЛЬКІСТЬ КІМНАТ (Тепер напряму в root)
            if (filter.getRoomsTotal() != null && !filter.getRoomsTotal().isEmpty()) {
                predicates.add(root.get("roomsTotal").in(filter.getRoomsTotal()));
            }

            // ПОШУК ПО МІСТУ ТА АДРЕСІ
            if (filter.getAddressSearch() != null && !filter.getAddressSearch().trim().isEmpty()) {
                String search = "%" + filter.getAddressSearch().toLowerCase() + "%";
                // Шукаємо в адресі АБО в назві міста
                Predicate addressMatch = cb.like(cb.lower(root.get("address")), search);
                Predicate cityMatch = cb.like(cb.lower(root.join("city").get("name")), search);
                predicates.add(cb.or(addressMatch, cityMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
}