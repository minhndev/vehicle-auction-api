package com.example.vehicle_auction.domain.event;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DepositPaymentProcessedEvent(
        UUID accountId,
        UUID auctionId,
        UUID depositId,
        String transactionRef,
        String gatewayTransactionNo,
        String vnpResponseCode,
        PaymentStatus paymentStatus,
        DepositStatus depositStatus,
        LocalDateTime occurredAt
) {
}

