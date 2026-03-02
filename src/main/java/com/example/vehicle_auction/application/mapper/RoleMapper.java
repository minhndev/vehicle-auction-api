package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.role.RoleRequest;
import com.example.vehicle_auction.application.dto.role.RoleResponse;
import com.example.vehicle_auction.application.dto.role.RoleUpdateRequest;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleRequest req);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    void updateRoleFromDto(RoleUpdateRequest dto, @MappingTarget Role role);
}
