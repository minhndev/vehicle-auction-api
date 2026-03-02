package com.example.vehicle_auction.domain.repository;

import java.util.UUID;

public interface DepositRepository {
    boolean hasPaidDeposit(UUID auctionId, UUID accountId);
}
