package com.example.vehicle_auction.infrastructure.messaging.dto;

import java.io.Serializable;
import java.util.UUID;

public record RefundMessage(
        UUID depositId,
        UUID auctionId,
        UUID accountId
) implements Serializable {
}
