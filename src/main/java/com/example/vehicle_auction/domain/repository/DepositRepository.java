package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.model.DepositModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepositRepository {
    boolean hasPaidDeposit(UUID auctionId, UUID accountId);

    List<DepositModel> findByAuctionIdAndStatus(UUID auctionId, DepositStatus status);

    Optional<DepositModel> findById(UUID id);

    Optional<DepositModel> findByTransactionReference(String transactionReference);

    Optional<DepositModel> findByAuctionIdAndAccountIdAndStatus(UUID auctionId, UUID accountId, DepositStatus status);

    DepositModel save(DepositModel depositModel);
}
