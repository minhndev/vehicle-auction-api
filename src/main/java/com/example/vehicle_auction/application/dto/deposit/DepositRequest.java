package com.example.vehicle_auction.application.dto.deposit;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DepositRequest(
        @NotNull(message = "Auction ID is required")
        UUID auctionId,

        String paymentMethod
) {
}
