package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.auction.AuctionRequest;
import com.example.vehicle_auction.application.dto.auction.AuctionResponse;
import com.example.vehicle_auction.domain.model.AuctionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuctionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actualEndTime", ignore = true)
    @Mapping(target = "currentPrice", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "winnerId", ignore = true)
    AuctionModel toDomain(AuctionRequest request);

    AuctionResponse toResponse(AuctionModel auctionModel);
}
