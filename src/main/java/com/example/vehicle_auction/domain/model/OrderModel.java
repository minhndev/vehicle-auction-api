package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderModel {
    private UUID id;
    private UUID auctionId;
    private UUID winnerId;
    private BigDecimal totalAmount;
    private BigDecimal remainingAmount;
    private OrderStatus status;
    private LocalDateTime paymentDeadDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
