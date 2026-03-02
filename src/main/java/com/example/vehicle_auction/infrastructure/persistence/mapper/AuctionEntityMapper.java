package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Auction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuctionEntityMapper {
    @Mapping(source = "product.id", target = "productId")
    AuctionModel toDomain(Auction entity);

    Auction toEntity(AuctionModel domain);

    void updateEntityFromModel(AuctionModel model, @MappingTarget Auction entity);
}
