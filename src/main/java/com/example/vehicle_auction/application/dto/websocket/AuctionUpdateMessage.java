package com.example.vehicle_auction.application.dto.websocket;

import java.math.BigDecimal;
import java.util.UUID;

public record AuctionUpdateMessage(
        UUID auctionId,
        BigDecimal currentPrice,
        UUID highestBidderId,
        String message
) {
}
