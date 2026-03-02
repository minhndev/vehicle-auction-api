package com.example.vehicle_auction.application.dto.auction;

import com.example.vehicle_auction.domain.enums.AuctionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AuctionResponse(
        UUID id,
        UUID productId,
        String productName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime actualEndTime,
        BigDecimal startPrice,
        BigDecimal currentPrice,
        BigDecimal bidIncrement,
        BigDecimal depositAmount,
        UUID winnerId,
        Integer version,
        AuctionStatus status,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) {
}
