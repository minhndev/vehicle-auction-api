package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.role.RoleRequest;
import com.example.vehicle_auction.application.dto.role.RoleResponse;
import com.example.vehicle_auction.application.dto.role.RoleUpdateRequest;
import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toResponse(RoleModel roleModel);

    @Mapping(target = "permissions", ignore = true)
    RoleModel toDomain(RoleRequest req);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    void updateRoleFromDto(RoleUpdateRequest dto, @MappingTarget RoleModel roleModel);
}
