package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.application.port.out.TransactionRepositoryPort;
import com.example.vehicle_auction.domain.enums.PaymentStatus;
import com.example.vehicle_auction.domain.model.TransactionModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Transaction;
import com.example.vehicle_auction.infrastructure.persistence.mapper.TransactionEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepositoryPort {
    private final JpaTransactionRepository jpaTransactionRepository;
    private final TransactionEntityMapper transactionEntityMapper;

    @Override
    public TransactionModel save(TransactionModel transactionModel) {
        Transaction entity;
        if (transactionModel.getId() != null && jpaTransactionRepository.existsById(transactionModel.getId())) {
            entity = jpaTransactionRepository.findById(transactionModel.getId()).orElseThrow();
            transactionEntityMapper.updateEntityFromModel(transactionModel, entity);
        } else {
            entity = transactionEntityMapper.toEntity(transactionModel);
        }

        Transaction savedEntity = jpaTransactionRepository.save(entity);
        return transactionEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<TransactionModel> findByGatewayReference(String gatewayReference) {
        return jpaTransactionRepository.findByGatewayReference(gatewayReference)
                .map(transactionEntityMapper::toDomain);
    }

    @Override
    public Page<TransactionModel> findByUserId(UUID userId, Pageable pageable) {
        return jpaTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(transactionEntityMapper::toDomain);
    }

    @Override
    public Optional<TransactionModel> findByReferenceIdAndTargetTypeAndStatus(UUID referenceId, String targetType, PaymentStatus status) {
        return jpaTransactionRepository.findByReferenceIdAndTargetTypeAndStatus(referenceId, targetType, status)
                .map(transactionEntityMapper::toDomain);
    }



}
