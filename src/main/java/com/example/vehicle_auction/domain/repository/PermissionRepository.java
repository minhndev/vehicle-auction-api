package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.PermissionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {
    PermissionModel save(PermissionModel permissionModel);

    Page<PermissionModel> findAll(Pageable pageable);

    List<PermissionModel> findAllById(List<UUID> ids);

    Optional<PermissionModel> findById(UUID id);

    boolean existsByName(String name);
}
