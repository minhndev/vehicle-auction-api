package com.example.vehicle_auction.application.usecase.permission;

import com.example.vehicle_auction.application.dto.permission.PermissionRequest;
import com.example.vehicle_auction.application.dto.permission.PermissionResponse;
import com.example.vehicle_auction.application.mapper.PermissionMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.PermissionModel;
import com.example.vehicle_auction.domain.repository.PermissionRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Permission;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePermissionUseCase {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionResponse execute(PermissionRequest req) {
        if (permissionRepository.existsByName(req.name())) {
            throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTS);
        }

        PermissionModel permissionModel = permissionMapper.toDomain(req);

        PermissionModel savedPermission = permissionRepository.save(permissionModel);
        return permissionMapper.toResponse(savedPermission);
    }
}
