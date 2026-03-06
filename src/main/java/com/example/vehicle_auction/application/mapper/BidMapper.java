package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.bid.BidRequest;
import com.example.vehicle_auction.application.dto.bid.BidResponse;
import com.example.vehicle_auction.infrastructure.persistence.entity.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BidMapper {

    @Mapping(target = "isWinning", ignore = true)
    BidResponse toResponse(Bid bid);

    default BidResponse toResponseWithWinningStatus(Bid bid, UUID currentWinnerId) {
        BidResponse response = toResponse(bid);

        return new BidResponse(
                response.id(),
                response.auctionId(),
                response.bidderId(),
                response.amount(),
                response.createdAt(),
                bid.getBidderId().equals(currentWinnerId)
        );
    }
}
