package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.BidModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BidRepository {
    BidModel save(BidModel bidModel);

    List<BidModel> findTop10ByAuctionId(UUID auctionId);

    Page<BidModel> findByBidderIdOrderByCreatedAtDesc(UUID bidderId, Pageable pageable);
}
