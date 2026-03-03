package com.example.vehicle_auction.application.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response object containing details of a successfully placed bid")
public record BidResponse(
        @Schema(description = "Unique ID of the bid record")
        UUID id,

        @Schema(description = "ID of the auction session")
        UUID auctionId,

        @Schema(description = "ID of the user who placed the bid")
        UUID bidderId,

        @Schema(description = "The accepted bid amount", example = "510000000.00")
        BigDecimal amount,

        @Schema(description = "Exact timestamp when the bid was recorded")
        LocalDateTime createdAt,

        @Schema(description = "Indicates if this bid is currently the highest winning bid", example = "true")
        boolean isWinning
) {}
