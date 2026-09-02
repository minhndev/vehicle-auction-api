package com.example.vehicle_auction.infrastructure.persistence.mapper;


import com.example.vehicle_auction.domain.model.BidModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Bid;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BidEntityMapper {
    BidModel toDomain(Bid entity);

    Bid toEntity(BidModel domain);
}
