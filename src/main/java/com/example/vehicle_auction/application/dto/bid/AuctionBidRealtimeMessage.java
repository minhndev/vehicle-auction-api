package com.example.vehicle_auction.application.dto.bid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AuctionBidRealtimeMessage(
        UUID auctionId,
        BigDecimal currentPrice,
        String message,
        UUID bidId,
        String bidderMask,
        BigDecimal amount,
        LocalDateTime createdAt
) {
}

