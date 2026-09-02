package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.PermissionModel;
import com.example.vehicle_auction.domain.repository.PermissionRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Permission;
import com.example.vehicle_auction.infrastructure.persistence.mapper.PermissionEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepository {
    private final JpaPermissionRepository jpaPermissionRepository;
    private final PermissionEntityMapper permissionEntityMapper;

    @Override
    public PermissionModel save(PermissionModel permissionModel) {
        Permission permissionEntity = permissionEntityMapper.toEntity(permissionModel);
        return permissionEntityMapper.toDomain(jpaPermissionRepository.save(permissionEntity));
    }

    @Override
    public Page<PermissionModel> findAll(Pageable pageable) {
        return jpaPermissionRepository.findAll(pageable)
                .map(permissionEntityMapper::toDomain);
    }

    @Override
    public List<PermissionModel> findAllById(List<UUID> ids) {
        return jpaPermissionRepository.findAllById(ids)
                .stream()
                .map(permissionEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<PermissionModel> findById(UUID id) {
        return jpaPermissionRepository.findById(id)
                .map(permissionEntityMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaPermissionRepository.existsByName(name);
    }
}
