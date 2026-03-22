package com.example.vehicle_auction.application.dto.order;

import com.example.vehicle_auction.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID auctionId,
        String productName,
        BigDecimal winningPrice,
        BigDecimal depositAmount,
        BigDecimal remainingAmount,
        OrderStatus status,
        LocalDateTime createdAt,

        String recipientName,
        String recipientPhone,
        String shippingAddress,
        String shippingNote
) {
}