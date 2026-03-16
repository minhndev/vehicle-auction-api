package com.example.vehicle_auction.application.dto.payment;

public record RefundRequest(
        String transactionReference,
        long amount,
        String transactionType,
        String transactionDate,
        String transactionNo,
        String createDate,
        String createBy
) {
}
