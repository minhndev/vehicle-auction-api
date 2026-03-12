package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.OrderStatus;
import com.example.vehicle_auction.domain.model.base.AuditModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class OrderModel extends AuditModel {
    private UUID auctionId;
    private UUID winnerId;
    private BigDecimal totalAmount;
    private BigDecimal remainingAmount;
    private OrderStatus status;
    private LocalDateTime paymentDeadDate;

}
