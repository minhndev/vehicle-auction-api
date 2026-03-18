package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.domain.repository.RoleRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Permission;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.mapper.RoleEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaPermissionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    private final JpaRoleRepository jpaRoleRepository;
    private final JpaPermissionRepository jpaPermissionRepository;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public RoleModel save(RoleModel roleModel) {
        Role roleEntity = roleEntityMapper.toEntity(roleModel);

        if (roleModel.getPermissions() != null) {
            Set<Permission> managedPermissions = roleModel.getPermissions().stream()
                    .map(pModel -> jpaPermissionRepository.getReferenceById(pModel.getId())) // Get ID from Domain Model
                    .collect(Collectors.toSet());

            roleEntity.setPermissions(managedPermissions);
        }

        return roleEntityMapper.toDomain(jpaRoleRepository.save(roleEntity));
    }

    @Override
    public Page<RoleModel> findAll(Pageable pageable) {
        return jpaRoleRepository.findAll(pageable)
                .map(roleEntityMapper::toDomain);
    }

    @Override
    public Optional<RoleModel> findById(UUID id) {
        return jpaRoleRepository.findById(id)
                .map(roleEntityMapper::toDomain);
    }

    @Override
    public Optional<RoleModel> findByName(String name) {
        return jpaRoleRepository.findByName(name)
                .map(roleEntityMapper::toDomain);
    }

    @Override
    public boolean existByName(String name) {
        return jpaRoleRepository.existsByName(name);
    }
}
