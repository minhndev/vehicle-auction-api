package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = {PermissionEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleEntityMapper {

    RoleModel toDomain(Role entity);

    Role toEntity(RoleModel domain);
}
