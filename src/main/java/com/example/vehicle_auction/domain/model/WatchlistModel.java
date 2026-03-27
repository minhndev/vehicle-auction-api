package com.example.vehicle_auction.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
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

}
