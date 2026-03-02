package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.AuctionModel;

import java.util.Optional;
import java.util.UUID;

public interface AuctionRepository {
    Optional<AuctionModel> findByIdWithLock(UUID id);
    AuctionModel save(AuctionModel auctionModel);
}
