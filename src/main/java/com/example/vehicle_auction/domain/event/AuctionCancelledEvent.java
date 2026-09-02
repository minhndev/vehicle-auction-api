package com.example.vehicle_auction.domain.event;

import java.util.UUID;

public record AuctionCancelledEvent(
        UUID auctionId,
        String cancelReason
) {
}
