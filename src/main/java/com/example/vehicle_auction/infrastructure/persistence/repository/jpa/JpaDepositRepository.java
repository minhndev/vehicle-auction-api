package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.infrastructure.persistence.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaDepositRepository extends JpaRepository<Deposit, UUID> {

    // check if a deposit exists for a given auction, user, and status
    boolean existsByAuctionIdAndAccountIdAndStatus(UUID auctionId, UUID accountId, DepositStatus status);
}