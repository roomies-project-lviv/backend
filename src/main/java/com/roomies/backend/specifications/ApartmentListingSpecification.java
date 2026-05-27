package com.roomies.backend.specifications;

import com.roomies.backend.dto.filters.ListingFilterDto;
import com.roomies.backend.models.ApartmentListing;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

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

            // ПОШУК У РАДІУСІ (POSTGIS)
            if (filter.getRadiusLat() != null && filter.getRadiusLng() != null && filter.getRadiusKm() != null) {
                GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
                Point center = gf.createPoint(new Coordinate(filter.getRadiusLng(), filter.getRadiusLat()));
                center.setSRID(4326); // Обов'язково вказуємо SRID

                // Радіус у метрах (км * 1000)
                double radiusInMeters = filter.getRadiusKm() * 1000.0;

                // Використовуємо ST_DistanceSphere, яка надійно повертає відстань у метрах
                // WHERE ST_DistanceSphere(location, center) <= radiusInMeters
                predicates.add(cb.lessThanOrEqualTo(
                    cb.function("ST_DistanceSphere", Double.class, root.get("location"), cb.literal(center)),
                    radiusInMeters
                ));
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

            // --- ФІЛЬТРАЦІЯ ЗРУЧНОСТЕЙ (JSONB) ---
            if (Boolean.TRUE.equals(filter.getWifi())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("wifi")), "true"));
            }
            if (Boolean.TRUE.equals(filter.getWashingMachine())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("washingMachine")), "true"));
            }
            if (Boolean.TRUE.equals(filter.getBoiler())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("boiler")), "true"));
            }
            if (Boolean.TRUE.equals(filter.getAirConditioner())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("airConditioner")), "true"));
            }
            if (Boolean.TRUE.equals(filter.getDishwasher())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("dishwasher")), "true"));
            }
            if (Boolean.TRUE.equals(filter.getElevator())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("elevator")), "true"));
            }
            if (Boolean.TRUE.equals(filter.getShelter())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("shelter")), "true"));
            }
            if (Boolean.TRUE.equals(filter.getParking())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("parking")), "true"));
            }
            if (Boolean.TRUE.equals(filter.getSecurity())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("security")), "true"));
            }
            if (Boolean.TRUE.equals(filter.getPetFriendly())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("petFriendly")), "true"));
            }
            if (Boolean.TRUE.equals(filter.getKidsFriendly())) {
                predicates.add(cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("amenities"), cb.literal("kidsFriendly")), "true"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}