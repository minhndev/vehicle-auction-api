package com.example.vehicle_auction.application.dto.payment;

import java.util.UUID;

public record CreatePaymentCommand(
        UUID userId,
        UUID referenceId,
        String targetType,
        long amount,
        String ipAddress
) { }
