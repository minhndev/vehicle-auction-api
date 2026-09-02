package com.example.vehicle_auction.domain.event;

import java.math.BigDecimal;
import java.util.UUID;


public record OutbidEvent(
        UUID previousWinnerId,
        UUID auctionId,
        BigDecimal newHighestAmount
) {}
