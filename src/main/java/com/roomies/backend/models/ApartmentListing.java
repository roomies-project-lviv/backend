package com.roomies.backend.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "apartment_listings")
public class ApartmentListing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(name = "price_per_month", nullable = false)
    private BigDecimal pricePerMonth;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // ДОДАЄМО ПОЛЯ З КВАРТИРИ:
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double area;

    @Column(nullable = false)
    private int roomsTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    @Column(name = "apartment_type")
    private String apartmentType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "image_urls", columnDefinition = "jsonb")
    private java.util.List<String> imageUrls = new java.util.ArrayList<>();

    // --- Гетери та Сетери ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public BigDecimal getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }

    public int getRoomsTotal() { return roomsTotal; }
    public void setRoomsTotal(int roomsTotal) { this.roomsTotal = roomsTotal; }

    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }
    
    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }
    
    public String getApartmentType() { return apartmentType; }
    public void setApartmentType(String apartmentType) { this.apartmentType = apartmentType; }

    public java.util.List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(java.util.List<String> imageUrls) { this.imageUrls = imageUrls; }

}