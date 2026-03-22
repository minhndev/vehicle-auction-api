package com.example.vehicle_auction.application.dto.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserManagementResponse(
        UUID id,
        String fullName,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        boolean active,
        boolean verified,
        boolean deleted,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<String> roles
) {
}

