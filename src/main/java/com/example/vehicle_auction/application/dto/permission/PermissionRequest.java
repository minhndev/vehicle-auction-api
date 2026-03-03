package com.example.vehicle_auction.application.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for creating a permission")
public record PermissionRequest(
        @Schema(description = "Unique name of the permission", example = "AUCTION_CREATE")
        @NotBlank(message = "{permission.name.required}")
        @Size(min = 3, max = 50, message = "{permission.name.size}")
        String name,

        @Schema(description = "Group name of the permission", example = "AUCTION")
        @NotBlank(message = "{permission.group_name.required}")
        String groupName,

        @Schema(description = "Brief description of the role's responsibilities", example = "Create new auctions")
        @NotBlank(message = "{permission.description.required}")
        String description
) { }
