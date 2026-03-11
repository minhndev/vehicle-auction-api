package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.model.base.FullModel;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RoleModel extends FullModel {
    private String name;
    private String description;
    private boolean system;
    private Set<PermissionModel> permissions;

    public RoleModel() {
        this.permissions = new HashSet<>();
    }

    public RoleModel(UUID id,
                     LocalDateTime createdAt,
                     LocalDateTime updatedAt,
                     String createdBy,
                     String updatedBy,
                     boolean deleted,
                     LocalDateTime deletedAt,
                     String name,
                     String description,
                     boolean system,
                     Set<PermissionModel> permissions
    ) {
        super(id, createdAt, updatedAt, createdBy, updatedBy, deleted, deletedAt);
        this.name = name;
        this.description = description;
        this.system = system;
        this.permissions = permissions;
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

    public Set<PermissionModel> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionModel> permissions) {
        this.permissions = permissions;
    }
}
