package com.example.vehicle_auction.application.usecase.role;

import com.example.vehicle_auction.application.dto.role.RoleResponse;
import com.example.vehicle_auction.application.dto.role.RoleUpdateRequest;
import com.example.vehicle_auction.application.mapper.RoleMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.PermissionModel;
import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.domain.repository.PermissionRepository;
import com.example.vehicle_auction.domain.repository.RoleRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaPermissionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateRoleUseCase {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleResponse execute(UUID id, RoleUpdateRequest req) {
        RoleModel roleModel = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        if (req.name() != null && !req.name().equals(roleModel.getName()) && roleRepository.existByName(req.name())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        roleMapper.updateRoleFromDto(req, roleModel);

        if (req.permissionIds() != null) {
            Set<PermissionModel> permissions = req.permissionIds().stream()
                    .map(permId -> permissionRepository.findById(permId)
                            .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)))
                    .collect(Collectors.toSet());

            roleModel.setPermissions(permissions);
        }
        
        RoleModel updatedRole = roleRepository.save(roleModel);
        return roleMapper.toResponse(updatedRole);
    }
}
