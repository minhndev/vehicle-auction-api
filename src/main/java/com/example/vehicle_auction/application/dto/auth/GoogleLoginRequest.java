package com.example.vehicle_auction.application.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record GoogleLoginRequest(
        @NotBlank(message = "ID Token is required")
        String idToken
) {
}
