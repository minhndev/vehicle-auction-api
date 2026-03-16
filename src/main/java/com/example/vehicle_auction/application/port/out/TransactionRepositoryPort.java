package com.example.vehicle_auction.application.port.out;

import com.example.vehicle_auction.domain.enums.PaymentStatus;
import com.example.vehicle_auction.domain.model.TransactionModel;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepositoryPort {
    TransactionModel save(TransactionModel transaction);
    Optional<TransactionModel> findByGatewayReference(String gatewayReference);

    Optional<TransactionModel> findByReferenceIdAndTargetTypeAndStatus(
            UUID referenceId,
            String targetType,
            PaymentStatus status
    );

}
