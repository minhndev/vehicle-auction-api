package com.example.vehicle_auction.application.usecase.role;

import com.example.vehicle_auction.application.dto.role.RoleRequest;
import com.example.vehicle_auction.application.dto.role.RoleResponse;
import com.example.vehicle_auction.application.mapper.RoleMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Permission;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaPermissionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateRoleUseCase {
    private final JpaRoleRepository jpaRoleRepository;
    private final JpaPermissionRepository jpaPermissionRepository;
    private final RoleMapper roleMapper;

    public RoleResponse execute(RoleRequest req) {
        if (jpaRoleRepository.existsByName(req.name()))
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        Role role = roleMapper.toEntity(req);
        if (req.permissionIds() != null) {
            List<Permission> permissions = jpaPermissionRepository.findAllById(req.permissionIds());
            role.setPermissions(new HashSet<>(permissions));
        }
        return roleMapper.toResponse(jpaRoleRepository.save(role));
    }
}
