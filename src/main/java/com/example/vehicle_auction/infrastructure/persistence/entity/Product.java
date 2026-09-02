package com.example.vehicle_auction.infrastructure.persistence.entity;

import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.infrastructure.persistence.entity.base.FullEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product extends FullEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "seller_id")
    private UUID sellerId;

    @Column(nullable = false)
    private String name;

    @Column(length = 100)
    private String brand;

    @Column(length = 100)
    private String model;

    @Column(length = 20)
    private String color;

    @Column(name = "vin_number", unique = true, length = 50)
    private String vinNumber;

    @Column(name = "engine_number", length = 50)
    private String engineNumber;

    @Column(name = "license_plate", length = 20)
    private String licensePlate;

    @Column(name = "manufacture_year")
    private Integer manufactureYear;

    private Integer mileage;

    @Column(length = 50)
    private String transmission;

    @Column(name = "fuel_type", length = 50)
    private String fuelType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_price", precision = 19, scale = 2)
    private BigDecimal startPrice;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProductStatus status;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @Version
    private Integer version;

    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
    }

}
