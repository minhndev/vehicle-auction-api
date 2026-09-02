package com.example.vehicle_auction.application.usecase.permission;

import com.example.vehicle_auction.application.dto.permission.PermissionResponse;
import com.example.vehicle_auction.application.mapper.PermissionMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.PermissionModel;
import com.example.vehicle_auction.domain.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPermissionUseCase {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public Page<PermissionResponse> getAll(Pageable pageable) {
        Page<PermissionModel> permissionPage = permissionRepository.findAll(pageable);
        return permissionPage.map(permissionMapper::toResponse);
    }

    public PermissionResponse getById(UUID id) {
        PermissionModel permissionModel = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        return permissionMapper.toResponse(permissionModel);
    }
}
