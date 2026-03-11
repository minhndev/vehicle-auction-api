package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.model.base.AuditModel;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }
}
