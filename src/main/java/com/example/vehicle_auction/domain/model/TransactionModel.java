package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.PaymentStatus;
import com.example.vehicle_auction.domain.model.base.BaseIdModel;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionModel extends BaseIdModel {
    private UUID userId;
    private UUID referenceId;
    private String targetType;
    private String gatewayReference;
    private long amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TransactionModel() {
    }

    public TransactionModel(UUID id,
                            UUID userId,
                            UUID referenceId,
                            String targetType,
                            String gatewayReference,
                            long amount,
                            PaymentStatus status,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt
    ) {
        super(id);
        this.userId = userId;
        this.referenceId = referenceId;
        this.targetType = targetType;
        this.gatewayReference = gatewayReference;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getGatewayReference() {
        return gatewayReference;
    }

    public void setGatewayReference(String gatewayReference) {
        this.gatewayReference = gatewayReference;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void markAsSuccess() {
        this.status = PaymentStatus.SUCCESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.status = PaymentStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }
}
