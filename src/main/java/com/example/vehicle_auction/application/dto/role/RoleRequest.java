package com.example.vehicle_auction.application.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

@Schema(description = "Request body for creating or updating a role")
public record RoleRequest(
        @Schema(description = "Unique name of the role", example = "MANAGER")
        @NotBlank(message = "{role.name.required}")
        @Size(min = 3, max = 50, message = "{role.name.size}")
        String name,
        @Schema(description = "Brief description of the role's responsibilities",
                example = "Staff responsible for approving vehicle listings")
        @NotBlank(message = "{role.description.required}")
        String description,
        @Schema(description = "List of permission UUIDs to be assigned to this role",
                example = "[\"550e8400-e29b-41d4-a716-446655440000\", \"670e8400-e29b-41d4-a716-446655440001\"]")
        Set<UUID> permissionIds) {
}
