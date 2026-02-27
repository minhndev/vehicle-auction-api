package com.example.vehicle_auction.application.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

@Schema(description = "Request body for updating a role")
public record RoleUpdateRequest(
        @Schema(example = "AUCTION_MODERATOR")
        @Size(min = 3, max = 50, message = "{role.name.size}")
        String name,
        @Schema(example = "Responsible for managing auction sessions")
        String description,
        @Schema(example = "true")
        boolean active,
        @Schema(description = "New set of permissions IDs")
        Set<UUID> permissionIds
) { }
