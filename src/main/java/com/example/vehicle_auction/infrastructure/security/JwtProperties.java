package com.example.vehicle_auction.infrastructure.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        @NotBlank
        String accessSecret,

        @NotBlank
        String refreshSecret,

        @Positive
        long accessExpiration,

        @Positive
        long refreshExpiration
) {
}
