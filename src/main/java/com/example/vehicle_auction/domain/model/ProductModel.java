package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.domain.model.base.FullModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class ProductModel extends FullModel {
    private UUID categoryId;
    private UUID sellerId;

    private String vinNumber;
    private String brand;
    private String model;
    private String color;
    private String name;
    private String engineNumber;
    private String licensePlate;
    private Integer manufactureYear;
    private Integer mileage;
    private String transmission;
    private String fuelType;
    private String description;

    private BigDecimal startPrice;
    private ProductStatus status;
    private boolean isActive;
    private Integer version;

    private List<ProductImageModel> images;

    public void addImage(ProductImageModel image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
    }
}
