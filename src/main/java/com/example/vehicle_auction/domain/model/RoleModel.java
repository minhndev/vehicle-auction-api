package com.example.vehicle_auction.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RoleModel {
    private UUID id;
    private String name;
    private String description;
    private Set<PermissionModel> permissions;

    public RoleModel() {
        this.permissions = new HashSet<>();
    }

    public RoleModel(UUID id, String name, String description, Set<PermissionModel> permissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = permissions != null ? permissions : new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Set<PermissionModel> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionModel> permissions) {
        this.permissions = permissions;
    }
}
