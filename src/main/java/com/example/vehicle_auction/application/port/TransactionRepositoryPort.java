package com.example.vehicle_auction.application.port;

import com.example.vehicle_auction.domain.model.TransactionModel;

public interface TransactionRepositoryPort {
    TransactionModel save(TransactionModel transaction);
}