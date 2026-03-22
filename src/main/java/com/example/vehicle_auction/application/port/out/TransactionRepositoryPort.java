package com.example.vehicle_auction.application.port.out;

import com.example.vehicle_auction.domain.enums.PaymentStatus;
import com.example.vehicle_auction.domain.model.TransactionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepositoryPort {
    TransactionModel save(TransactionModel transaction);
    Optional<TransactionModel> findByGatewayReference(String gatewayReference);
    Page<TransactionModel> findByUserId(UUID userId, Pageable pageable);

    Optional<TransactionModel> findByReferenceIdAndTargetTypeAndStatus(
            UUID referenceId,
            String targetType,
            PaymentStatus status
    );

}
