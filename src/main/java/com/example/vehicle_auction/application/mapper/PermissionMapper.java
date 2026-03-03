package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.permission.PermissionRequest;
import com.example.vehicle_auction.application.dto.permission.PermissionResponse;
import com.example.vehicle_auction.infrastructure.persistence.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toResponse(Permission permission);

    Permission toEntity(PermissionRequest req);
}
