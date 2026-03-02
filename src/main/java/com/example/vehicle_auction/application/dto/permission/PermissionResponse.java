package com.example.vehicle_auction.application.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record PermissionResponse(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,
        @Schema(example = "AUCTION")
        String groupName,
        @Schema(example = "AUCTION_CREATE")
        String name,
        @Schema(example = "Allow user to create new auction sessions")
        String description,
        @Schema(example = "true")
        boolean system,
        @Schema(example = "true")
        boolean active,
        @Schema(example = "2024-01-01T12:00:00")
        LocalDateTime createdAt,
        @Schema(example = "2024-01-01T12:00:00")
        LocalDateTime updatedAt,
        @Schema(example = "SYSTEM")
        String createdBy,
        @Schema(example = "SYSTEM")
        String updatedBy
) {
}
