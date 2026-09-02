package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.deposit.DepositResponse;
import com.example.vehicle_auction.domain.model.DepositModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepositMapper {
    DepositResponse toResponse(DepositModel depositModel);
}
