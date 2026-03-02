package com.example.vehicle_auction.domain.model;

import java.util.UUID;

public class PermissionModel {
    private UUID id;
    private String groupName;
    private String name;
    private String description;

    public PermissionModel() {
    }

    public PermissionModel(UUID id, String groupName, String name, String description) {
        this.id = id;
        this.groupName = groupName;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
