package com.example.vehicle_auction.application.port.out;

import com.example.vehicle_auction.domain.model.TransactionModel;

import java.util.Optional;

public interface TransactionRepositoryPort {
    TransactionModel save(TransactionModel transaction);
    Optional<TransactionModel> findByGatewayReference(String gatewayReference);
}
