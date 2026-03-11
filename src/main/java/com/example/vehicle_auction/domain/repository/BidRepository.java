package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.BidModel;

import java.util.List;
import java.util.UUID;

public interface BidRepository {
    BidModel save(BidModel bidModel);

    List<BidModel> findTop10ByAuctionId(UUID auctionId);
}
