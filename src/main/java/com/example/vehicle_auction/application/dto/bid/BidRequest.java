package com.example.vehicle_auction.application.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Request body for placing a bid in an active auction")
public record BidRequest(
        @Schema(description = "ID of the auction session", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "{bid.auction.required}")
        UUID auctionId,

        @Schema(description = "The amount the user wants to bid", example = "510000000.00")
        @NotNull(message = "{bid.amount.required}")
        @Positive(message = "{bid.amount.positive}")
        BigDecimal amount
) {}
