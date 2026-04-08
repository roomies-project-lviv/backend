package com.roomies.backend.specifications;

import com.roomies.backend.dto.filters.ListingFilterDto;
import com.roomies.backend.models.ApartmentListing;
import com.roomies.backend.models.Apartment;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ApartmentListingSpecification {

    public static Specification<ApartmentListing> withFilter(ListingFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<ApartmentListing, Apartment> apartment = root.join("apartment");

            predicates.add(cb.isTrue(root.get("isActive")));

            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerMonth"), filter.getMinPrice()));
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerMonth"), filter.getMaxPrice()));
            }

            if (filter.getRoomsTotal() != null && !filter.getRoomsTotal().isEmpty()) {
                predicates.add(apartment.get("roomsTotal").in(filter.getRoomsTotal()));
            }

            if (filter.getAddressSearch() != null && !filter.getAddressSearch().trim().isEmpty()) {
                String searchPattern = "%" + filter.getAddressSearch().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(apartment.get("address")), searchPattern));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
}