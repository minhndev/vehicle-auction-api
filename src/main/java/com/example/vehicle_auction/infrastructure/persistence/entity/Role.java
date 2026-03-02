package com.example.vehicle_auction.infrastructure.persistence.entity;

import com.example.vehicle_auction.infrastructure.persistence.entity.base.AuditEntity;
import com.example.vehicle_auction.infrastructure.persistence.entity.base.FullEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Where(clause = "is_deleted = false")
@Getter
@Setter
public class Role extends FullEntity {
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_system", nullable = false)
    private boolean system = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Set<Permission> permissions = new HashSet<>();
}