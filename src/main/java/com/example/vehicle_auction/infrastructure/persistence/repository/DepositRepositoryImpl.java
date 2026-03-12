package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.model.DepositModel;
import com.example.vehicle_auction.domain.repository.DepositRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Deposit;
import com.example.vehicle_auction.infrastructure.persistence.mapper.DepositEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaDepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DepositRepositoryImpl implements DepositRepository {

    private final JpaDepositRepository jpaDepositRepository;
    private final DepositEntityMapper depositEntityMapper;

    @Override
    public boolean hasPaidDeposit(UUID auctionId, UUID accountId) {
        return jpaDepositRepository.existsByAuctionIdAndAccountIdAndStatus(
                auctionId,
                accountId,
                DepositStatus.PAID
        );
    }

    @Override
    public Optional<DepositModel> findById(UUID id) {
        return jpaDepositRepository.findById(id).map(depositEntityMapper::toDomain);
    }

    @Override
    public Optional<DepositModel> findByTransactionReference(String transactionReference) {
        return jpaDepositRepository.findByTransactionReference(transactionReference)
                .map(depositEntityMapper::toDomain);
    }

    @Override
    public DepositModel save(DepositModel depositModel) {
        Deposit entity;
        if (depositModel.getId() != null) {
            entity = jpaDepositRepository.findById(depositModel.getId()).orElseThrow();
            depositEntityMapper.updateEntityFromModel(depositModel, entity);
        } else {
            entity = depositEntityMapper.toEntity(depositModel);
        }
        return depositEntityMapper.toDomain(jpaDepositRepository.save(entity));
    }
}
