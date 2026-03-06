package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.infrastructure.persistence.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaBidRepository extends JpaRepository<Bid, UUID> {

    List<Bid> findTop10ByAuctionIdOrderByAmountDesc(UUID auctionId);
}