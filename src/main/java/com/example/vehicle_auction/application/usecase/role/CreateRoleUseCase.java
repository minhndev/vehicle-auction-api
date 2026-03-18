package com.example.vehicle_auction.application.usecase.role;

import com.example.vehicle_auction.application.dto.role.RoleRequest;
import com.example.vehicle_auction.application.dto.role.RoleResponse;
import com.example.vehicle_auction.application.mapper.RoleMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.PermissionModel;
import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.domain.repository.PermissionRepository;
import com.example.vehicle_auction.domain.repository.RoleRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Permission;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaPermissionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateRoleUseCase {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleResponse execute(RoleRequest req) {
        if (roleRepository.existByName(req.name())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        RoleModel roleModel = roleMapper.toDomain(req);

        if (req.permissionIds() != null && !req.permissionIds().isEmpty()) {
//            Set<PermissionModel> permissions = req.permissionIds().stream()
//                    .map(id -> permissionRepository.findById(id)
//                            .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)))
//                    .collect(Collectors.toSet());
//
//            roleModel.setPermissions(permissions);
            List<PermissionModel> permissions = permissionRepository.findAllById(req.permissionIds());

            if (permissions.size() != req.permissionIds().size()) {
                throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
            }

            roleModel.setPermissions(new HashSet<>(permissions));
        }

        RoleModel savedRole = roleRepository.save(roleModel);

        return roleMapper.toResponse(savedRole);
    }
}
