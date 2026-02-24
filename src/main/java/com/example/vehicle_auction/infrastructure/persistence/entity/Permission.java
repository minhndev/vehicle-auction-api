package com.example.vehicle_auction.infrastructure.persistence.entity;

import com.example.vehicle_auction.infrastructure.persistence.entity.base.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission extends AuditEntity {
    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 250)
    private String description;

    @Column(name = "is_system", nullable = false)
    private boolean system = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
