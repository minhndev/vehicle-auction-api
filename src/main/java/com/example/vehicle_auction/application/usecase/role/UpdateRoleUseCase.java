package com.example.vehicle_auction.application.usecase.role;

import com.example.vehicle_auction.application.dto.role.RoleResponse;
import com.example.vehicle_auction.application.dto.role.RoleUpdateRequest;
import com.example.vehicle_auction.application.mapper.RoleMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaPermissionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateRoleUseCase {
    private final JpaRoleRepository jpaRoleRepository;
    private final JpaPermissionRepository jpaPermissionRepository;
    private final RoleMapper roleMapper;

    public RoleResponse execute(UUID id, RoleUpdateRequest req) {
        Role role = jpaRoleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_ALREADY_EXISTS));

        if (req.name() != null && !req.name().equals(role.getName()) && jpaRoleRepository.existsByName(req.name()))
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);

        roleMapper.updateRoleFromDto(req, role);

        if (req.permissionIds() != null) {
            var permissions = jpaPermissionRepository.findAllById(req.permissionIds());
            role.setPermissions(new HashSet<>(permissions));
        }

        return roleMapper.toResponse(jpaRoleRepository.save(role));
    }
}
