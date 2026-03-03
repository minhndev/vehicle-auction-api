package com.example.vehicle_auction.application.usecase.permission;

import com.example.vehicle_auction.application.dto.permission.PermissionRequest;
import com.example.vehicle_auction.application.dto.permission.PermissionResponse;
import com.example.vehicle_auction.application.mapper.PermissionMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Permission;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePermissionUseCase {
    private final JpaPermissionRepository jpaPermissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionResponse execute(PermissionRequest req) {
        if (jpaPermissionRepository.existsByName(req.name()))
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        Permission permission = permissionMapper.toEntity(req);
        return permissionMapper.toResponse(jpaPermissionRepository.save(permission));
    }
}
