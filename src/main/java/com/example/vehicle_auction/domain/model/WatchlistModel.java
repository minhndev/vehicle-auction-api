package com.example.vehicle_auction.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class WatchlistModel {
    private UUID id;
    private UUID accountId;
    private UUID productId;
    private LocalDateTime createdAt;

    public WatchlistModel() {
    }

    public WatchlistModel(UUID id, UUID accountId, UUID productId, LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.productId = productId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
