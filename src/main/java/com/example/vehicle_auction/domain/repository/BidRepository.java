package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.BidModel;

public interface BidRepository {
    BidModel save(BidModel bidModel);
}
