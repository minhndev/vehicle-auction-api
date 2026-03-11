package com.example.vehicle_auction.domain.model.base;

import java.util.UUID;

public abstract class BaseIdModel {
    private UUID id;

    public BaseIdModel() {
    }

    public BaseIdModel(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
