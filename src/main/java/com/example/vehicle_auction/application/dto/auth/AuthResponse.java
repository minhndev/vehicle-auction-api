package com.example.vehicle_auction.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing authentication tokens after successful login or registration")
public record AuthResponse(
        @Schema(description = "JWT Access Token for authenticating API requests", example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken,

        @Schema(description = "Refresh Token used to obtain a new access token", example = "d9b2f3e4-c5a6-4b7d-8e9f-1a2b3c4d5e6f")
        String refreshToken,

        @Schema(description = "The type of token", example = "Bearer")
        String tokenType
) { }
