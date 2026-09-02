package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.model.base.FullModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
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

}
