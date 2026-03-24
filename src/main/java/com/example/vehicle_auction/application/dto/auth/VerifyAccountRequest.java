package com.example.vehicle_auction.application.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyAccountRequest(
        @NotBlank(message = "{account.email.required}")
        @Email(message = "{account.email.invalid}")
        String email,

        @NotBlank(message = "{account.otp.required}")
        String otp
) {
}
