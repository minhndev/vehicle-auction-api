package com.example.vehicle_auction.domain.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public abstract class FullModel extends AuditModel {
    private boolean deleted;
    private LocalDateTime deletedAt;

    public FullModel() {
    }

    public FullModel(UUID id,
                     LocalDateTime createdAt,
                     LocalDateTime updatedAt,
                     String createdBy,
                     String updatedBy,
                     boolean deleted,
                     LocalDateTime deletedAt
    ) {
        super(id, createdAt, updatedAt, createdBy, updatedBy);
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deleted = false;
        this.deletedAt = null;
    }
}
