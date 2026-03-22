package com.example.vehicle_auction.application.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = "{account.reset_token.required}")
        String token,

        @NotBlank(message = "{account.password.required}")
        String newPassword,

        @NotBlank(message = "{account.confirm_password.required}")
        String confirmPassword
) {
}

