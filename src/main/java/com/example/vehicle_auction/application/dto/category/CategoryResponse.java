package com.example.vehicle_auction.application.dto.category;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String slug,
        String description,
        boolean active
) {
}
