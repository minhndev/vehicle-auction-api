package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.infrastructure.persistence.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaPermissionRepository extends JpaRepository<Permission, UUID> {
    boolean existsByName(String name);
}
