package com.example.vehicle_auction.application.dto.ProductImage;

import java.util.UUID;

public record ProductImageResponse(
        UUID id,
        String url,
        boolean main,
        Integer sortOrder) {
}
