package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DepositRepositoryImpl implements DepositRepository {

    private final JpaDepositRepository jpaDepositRepository;

    @Override
    public boolean hasPaidDeposit(UUID auctionId, UUID accountId) {
        return jpaDepositRepository.existsByAuctionIdAndAccountIdAndStatus(
                auctionId,
                accountId,
                DepositStatus.PAID
        );
    }
}
