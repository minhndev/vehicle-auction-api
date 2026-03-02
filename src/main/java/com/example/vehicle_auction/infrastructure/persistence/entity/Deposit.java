package com.example.vehicle_auction.infrastructure.persistence.entity;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.infrastructure.persistence.entity.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "deposits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deposit extends AuditEntity {

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "auction_id", nullable = false)
    private UUID auctionId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepositStatus status;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "transaction_reference", length = 100)
    private String transactionReference;
}
