package com.example.vehicle_auction.application.dto.role;

import com.example.vehicle_auction.application.dto.permission.PermissionResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record RoleResponse(

        @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(example = "MANAGER")
        String name,

        @Schema(example = "Staff responsible for approving vehicle listings")
        String description,

        @Schema(example = "true")
        boolean system,

        @Schema(example = "true")
        boolean active,

        @Schema(example = "[{\"id\": \"550e8400-e29b-41d4-a716-446655440000\", \"groupName\": \"AUCTION\", \"name\": \"AUCTION_CREATE\", \"description\": \"Allow user to create new auction sessions\", \"system\": true, \"active\": true, \"createdAt\": \"2024-01-01T12:00:00\", \"updatedAt\": \"2024-01-01T12:00:00\", \"createdBy\": \"SYSTEM\", \"updatedBy\": \"SYSTEM\"}]")
        Set<PermissionResponse> permissions,

        @Schema(example = "2024-01-01T12:00:00")
        LocalDateTime createdAt,

        @Schema(example = "2024-01-01T12:00:00")
        LocalDateTime updatedAt,

        @Schema(example = "SYSTEM")
        String createdBy,

        @Schema(example = "SYSTEM")
        String updatedBy,
        @Schema(example = "true")
        boolean deleted,
        @Schema(example = "2024-01-01T12:00:00")
        LocalDateTime deletedAt) {
}
