package com.example.vehicle_auction.application.dto.payment;

import com.example.vehicle_auction.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID userId,
        UUID referenceId,
        String targetType,
        String gatewayReference,
        long amount,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

