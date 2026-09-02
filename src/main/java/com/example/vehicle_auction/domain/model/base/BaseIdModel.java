package com.example.vehicle_auction.domain.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
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
