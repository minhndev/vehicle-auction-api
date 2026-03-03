package com.example.vehicle_auction.application.dto.deposit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DepositResponse(
        UUID id,
        UUID accountId,
        UUID auctionId,
        BigDecimal amount,
        String status,
        String paymentMethod,
        String transactionReference,
        LocalDateTime createdAt
) {
}
