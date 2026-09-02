package com.example.vehicle_auction.application.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "{account.refresh_token.required}")
        String refreshToken
) {}
