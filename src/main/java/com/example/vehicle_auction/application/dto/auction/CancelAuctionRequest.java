package com.example.vehicle_auction.application.dto.auction;

import jakarta.validation.constraints.NotBlank;

public record CancelAuctionRequest(
        @NotBlank(message = "Lý do hủy không được để trống")
        String reason
) {
}
