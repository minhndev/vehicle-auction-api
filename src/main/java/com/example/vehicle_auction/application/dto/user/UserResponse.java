package com.example.vehicle_auction.application.dto.user;

import com.example.vehicle_auction.domain.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String identityNumber,
        LocalDate birthdate,
        Gender gender,
        String phoneNumber,
        String address,
        String avatarURL,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean deleted,
        LocalDateTime deletedAt
) { }
