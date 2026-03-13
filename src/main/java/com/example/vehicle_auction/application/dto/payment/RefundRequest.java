package com.example.vehicle_auction.application.dto.payment;

public record RefundRequest(
        String transactionReference,
        long amount,
        String refundType,
        String transactionDate,
        String createBy
) {
}
