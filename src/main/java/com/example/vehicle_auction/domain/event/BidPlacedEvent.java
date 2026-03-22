package com.example.vehicle_auction.domain.event;

import java.math.BigDecimal;
import java.util.UUID;

public record BidPlacedEvent(
        UUID auctionId,
        BigDecimal newPrice,
        UUID accountId
) {
}
