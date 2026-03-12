package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.DepositModel;

import java.util.Optional;
import java.util.UUID;

public interface DepositRepository {
    boolean hasPaidDeposit(UUID auctionId, UUID accountId);

    Optional<DepositModel> findById(UUID id);

    Optional<DepositModel> findByTransactionReference(String transactionReference);

    DepositModel save(DepositModel depositModel);
}
