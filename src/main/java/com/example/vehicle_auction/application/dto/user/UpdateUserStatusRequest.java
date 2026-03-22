package com.example.vehicle_auction.application.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for updating user status")
public record UpdateUserStatusRequest(
        @Schema(example = "true", description = "Set account active/inactive")
        @NotNull(message = "{user.status.active.required}")
        Boolean active
) { }

