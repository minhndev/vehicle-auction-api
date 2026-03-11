package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.bid.BidResponse;
import com.example.vehicle_auction.domain.model.BidModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BidMapper {

    @Mapping(target = "isWinning", ignore = true)
    BidResponse toResponse(BidModel bidModel);

    default BidResponse toResponseWithWinningStatus(BidModel bidModel, boolean isWinning) {
        BidResponse response = toResponse(bidModel);

        return new BidResponse(
                response.id(),
                response.auctionId(),
                response.bidderId(),
                response.amount(),
                response.createdAt(),
                isWinning
        );
    }
}
