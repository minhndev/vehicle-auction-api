package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.auction.AuctionRequest;
import com.example.vehicle_auction.application.dto.auction.AuctionResponse;
import com.example.vehicle_auction.infrastructure.persistence.entity.Auction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuctionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "actualEndTime", ignore = true)
    @Mapping(target = "currentPrice", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Auction toEntity(AuctionRequest request);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    AuctionResponse toResponse(Auction auction);
}
