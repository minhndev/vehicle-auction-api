package com.example.vehicle_auction.application.dto.watchlist;

import java.time.LocalDateTime;
import java.util.UUID;

public record WatchlistResponse(
        UUID id,
        UUID accountId,
        UUID productId,
        LocalDateTime createdAt
) {
}

