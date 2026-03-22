package com.example.vehicle_auction.application.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(
        @NotBlank(message = "Name is required")
        String recipientName,

        @NotBlank(message = "Phone number is required")
        String recipientPhone,

        @NotBlank(message = "Address is required")
        String shippingAddress,

        String shippingNote
) {
}
