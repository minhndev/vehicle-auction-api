package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.infrastructure.persistence.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaDepositRepository extends JpaRepository<Deposit, UUID> {

    // check if a deposit exists for a given auction, user, and status
    boolean existsByAuctionIdAndAccountIdAndStatus(UUID auctionId, UUID accountId, DepositStatus status);

    Optional<Deposit> findByTransactionReference(String transactionReference);
}