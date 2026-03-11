package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.PermissionModel;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {
    PermissionModel save(PermissionModel permissionModel);

    Optional<PermissionModel> findById(UUID id);

    boolean existsByName(String name);
}
