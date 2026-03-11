package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.RoleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    RoleModel save(RoleModel roleModel);

    Page<RoleModel> findAll(Pageable pageable);

    Optional<RoleModel> findById(UUID id);

    Optional<RoleModel> findByName(String name);

    boolean existByName(String name);
}
