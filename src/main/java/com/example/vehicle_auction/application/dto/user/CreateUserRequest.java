package com.example.vehicle_auction.application.dto.user;

import com.example.vehicle_auction.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Request body for user creation")
public record CreateUserRequest(
        @Schema(description = "User's email", example = "user@example.com")
        @NotBlank(message = "{account.email.required}")
        @Email(message = "{account.email.invalid}")
        String email,

        @Schema(description = "User's password", example = "user@123")
        @NotBlank(message = "{account.password.required}")
        String password,

        @Schema(description = "User's confirm password", example = "user@123")
        @NotBlank(message = "{account.confirm_password.required}")
        String confirmPassword,

        @Schema(description = "User's first name", example = "John")
        @NotBlank(message = "{user.first_name.required}")
        String firstName,

        @Schema(description = "User's last name", example = "Doe")
        @NotBlank(message = "{user.last_name.required}")
        String lastName,

        @Schema(description = "User's identity number", example = "1234567890")
        @NotBlank(message = "{user.identity_number.required}")
        String identityNumber,

        @Schema(description = "User's birthdate", example = "1990-01-01")
        @NotNull(message = "{user.birthdate.required}")
        LocalDate birthdate,

        @Schema(description = "User's gender", example = "MALE")
        @NotNull(message = "{user.gender.required}")
        Gender gender,

        @Schema(description = "User's phone number", example = "+1234567890")
        @Pattern(regexp = "^\\+?[0-9.]{10,15}$", message = "{user.phone_number.invalid}")
        String phoneNumber,

        @Schema(description = "Physical address", example = "123 Main St, Springfield")
        String address,

        @Schema(description = "URL to the user's avatar image", example = "https://example.com/avatar.jpg")
        String avatarURL,

        @Schema(description = "Set of role names to assign to the user", example = "[\"USER\", \"ADMIN\"]")
        @NotEmpty(message = "{account.roles.required}")
        Set<String> roleNames
) { }
