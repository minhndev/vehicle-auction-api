package com.example.vehicle_auction.application.usecase.payment;

import com.example.vehicle_auction.application.dto.payment.TransactionResponse;
import com.example.vehicle_auction.application.port.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMyTransactionsUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;

    public Page<TransactionResponse> execute(UUID userId, Pageable pageable) {
        return transactionRepositoryPort.findByUserId(userId, pageable)
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getUserId(),
                        transaction.getReferenceId(),
                        transaction.getTargetType(),
                        transaction.getGatewayReference(),
                        transaction.getAmount(),
                        transaction.getStatus(),
                        transaction.getCreatedAt(),
                        transaction.getUpdatedAt()
                ));
    }
}

