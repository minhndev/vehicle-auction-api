package com.example.vehicle_auction.application.dto.user;

import com.example.vehicle_auction.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Request body for user update by Admin")
public record UpdateUserRequest(
        @Schema(description = "User's first name", example = "John")
        String firstName,

        @Schema(description = "User's last name", example = "Doe")
        String lastName,

        @Schema(description = "User's phone number", example = "+1234567890")
        @Pattern(regexp = "^\\+?[0-9.]{10,15}$", message = "{user.phone_number.invalid}")
        String phoneNumber,

        @Schema(description = "Physical address", example = "123 Main St, Springfield")
        String address,

        @Schema(description = "User's identity number (CCCD/ID card)", example = "012345678901")
        String identityNumber,

        @Schema(description = "URL to the user's avatar image", example = "https://example.com/avatar.jpg")
        String avatarURL,

        @Schema(description = "Set of role names to update", example = "[\"USER\", \"ADMIN\"]")
        Set<String> roleNames
) { }