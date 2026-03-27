package com.example.vehicle_auction.infrastructure.persistence.entity;

import com.example.vehicle_auction.domain.enums.PaymentStatus;
import com.example.vehicle_auction.infrastructure.persistence.entity.base.BaseIdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction extends BaseIdEntity {
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "target_type", length = 50)
    private String targetType;

    @Column(name = "gateway_reference", unique = true)
    private String gatewayReference;

    @Column(nullable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
