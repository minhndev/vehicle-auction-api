package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.model.base.AuditModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PermissionModel extends AuditModel {
    private String groupName;
    private String name;
    private String description;
    private boolean system;

    public PermissionModel() {
    }

    public PermissionModel(UUID id,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt,
                           String createdBy,
                           String updatedBy,
                           String groupName,
                           String name,
                           String description,
                           boolean system
    ) {
        super(id, createdAt, updatedAt, createdBy, updatedBy);
        this.groupName = groupName;
        this.name = name;
        this.description = description;
        this.system = system;
    }

}
