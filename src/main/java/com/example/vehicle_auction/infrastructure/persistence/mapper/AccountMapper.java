package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = {RoleEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {

    AccountModel toDomain(Account entity);

    Account toEntity(AccountModel domain);

    void updateEntityFromModel(AccountModel domain, @MappingTarget Account entity);
}