package com.example.vehicle_auction.infrastructure.persistence.entity;

import com.example.vehicle_auction.domain.enums.OrderStatus;
import com.example.vehicle_auction.infrastructure.persistence.entity.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends AuditEntity {

    @Column(name = "auction_id", nullable = false)
    private UUID auctionId;

    @Column(name = "winner_id", nullable = false)
    private UUID winnerId;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "remaining_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "payment_deadline")
    private LocalDateTime paymentDeadDate;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "recipient_phone")
    private String recipientPhone;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_note")
    private String shippingNote;
}
