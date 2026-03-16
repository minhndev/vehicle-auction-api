package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.domain.enums.PaymentStatus;
import com.example.vehicle_auction.infrastructure.persistence.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaTransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByGatewayReference(String gatewayReference);
    Optional<Transaction> findByReferenceIdAndTargetTypeAndStatus(
            UUID referenceId,
            String targetType,
            PaymentStatus status
    );
}

