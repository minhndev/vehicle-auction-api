package com.example.vehicle_auction.application.dto.payment;

public record PaymentRequest(
        String referenceId,
        long amount,
        String orderInfo,
        String ipAddress
) { }
