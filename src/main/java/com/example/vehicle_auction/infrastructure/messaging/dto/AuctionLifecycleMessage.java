package com.example.vehicle_auction.infrastructure.messaging.dto;

import java.io.Serializable;
import java.util.UUID;

public record AuctionLifecycleMessage(
        UUID auctionId,
        String action // "START" or "END"
) implements Serializable {
}
