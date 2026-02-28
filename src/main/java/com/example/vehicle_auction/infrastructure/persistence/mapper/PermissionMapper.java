package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.PermissionModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    PermissionModel toDomain(Permission entity);

    Permission toEntity(PermissionModel domain);
}
