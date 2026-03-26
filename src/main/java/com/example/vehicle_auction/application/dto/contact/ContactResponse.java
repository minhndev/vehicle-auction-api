package com.example.vehicle_auction.application.dto.contact;

import com.example.vehicle_auction.domain.enums.ContactStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContactResponse(
        UUID id,
        String fullName,
        String phoneNumber,
        String email,
        String subject,
        String content,
        ContactStatus status,
        LocalDateTime createdAt
) { }
