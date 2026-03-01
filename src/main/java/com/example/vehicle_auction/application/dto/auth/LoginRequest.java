package com.example.vehicle_auction.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body for user login")
public record LoginRequest(
        @Schema(example = "user@example.com")
        @NotBlank(message = "{account.email.required}")
        @Email(message = "{account.email.invalid}")
        String email,

        @Schema(example = "user@123")
        @NotBlank(message = "{account.password.required}")
        String password
) { }
