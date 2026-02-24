package com.example.vehicle_auction.application.usecase.role;

import com.example.vehicle_auction.application.dto.role.RoleRequest;
import com.example.vehicle_auction.application.dto.role.RoleResponse;
import com.example.vehicle_auction.application.mapper.RoleMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Permission;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.repository.PermissionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateRoleUseCase {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleResponse execute(RoleRequest req) {
        if (roleRepository.existsByName(req.name()))
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        Role role = roleMapper.toEntity(req);
        if (req.permissionIds() != null) {
            List<Permission> permissions = permissionRepository.findAllById(req.permissionIds());
            role.setPermissions(new HashSet<>(permissions));
        }
        return roleMapper.toResponse(roleRepository.save(role));
    }
}
