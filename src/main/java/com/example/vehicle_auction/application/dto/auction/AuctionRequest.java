package com.example.vehicle_auction.application.dto.auction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Request body to create a new auction session")
public record AuctionRequest(

        @NotNull(message = "{auction.product.required}")
        UUID productId,

        @Schema(description = "ISO-8601 format", example = "2026-03-01T10:00:00")
        @NotNull(message = "{auction.start_time.required}")
        @Future(message = "{auction.start_time.future}")
        LocalDateTime startTime,

        @Schema(description = "ISO-8601 format", example = "2026-03-03T10:00:00")
        @NotNull(message = "{auction.end_time.required}")
        @Future(message = "{auction.end_time.future}")
        LocalDateTime endTime,

        @NotNull(message = "{auction.start_price.required}")
        @Positive(message = "{auction.price.positive}")
        BigDecimal startPrice,

        @NotNull(message = "{auction.bid_increment.required}")
        @Positive(message = "{auction.price.positive}")
        BigDecimal bidIncrement,

        @NotNull(message = "{auction.deposit.required}")
        @Positive(message = "{auction.price.positive}")
        BigDecimal depositAmount
) {}
