package com.example.vehicle_auction.application.dto.product;

import com.example.vehicle_auction.application.dto.ProductImage.ProductImageResponse;
import com.example.vehicle_auction.domain.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String brand,
        String model,
        String vinNumber,

        String color,
        String engineNumber,
        String licensePlate,
        String transmission,
        String fuelType,
        String description,
        Integer manufactureYear,
        Integer mileage,
        BigDecimal startPrice,
        ProductStatus status,
        UUID categoryId,
        String categoryName,
        List<ProductImageResponse> images
) {
}
