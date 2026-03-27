package com.example.vehicle_auction.application.dto.payment;

import java.util.UUID;

public record PaymentCreateRequest(
        UUID referenceId,
        String targetType,
        long amount
) {}
