package com.example.vehicle_auction.application.listener;

import com.example.vehicle_auction.application.usecase.deposit.RefundDepositUseCase;
import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.event.AuctionCancelledEvent;
import com.example.vehicle_auction.domain.model.DepositModel;
import com.example.vehicle_auction.domain.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionCancelledEventListener {

    private final DepositRepository depositRepository;
    private final RefundDepositUseCase refundDepositUseCase;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAuctionCancelled(AuctionCancelledEvent event) {
        UUID auctionId = event.auctionId(); // Get cancelled auction ID
        log.info("[ASYNC WORKER] Starting refund scan for CANCELLED auction: {}", auctionId);

        // 1. Find ALL paid deposits for this auction
        List<DepositModel> allPaidDeposits = depositRepository.findByAuctionIdAndStatus(auctionId, DepositStatus.PAID);

        if (allPaidDeposits.isEmpty()) {
            log.info("No deposits to refund for auction: {}", auctionId);
            return;
        }

        // 2. Iterate and refund EVERYONE (since the auction was cancelled)
        for (DepositModel deposit : allPaidDeposits) {
            try {
                // Pass the correct DEPOSIT_ID to the UseCase
                refundDepositUseCase.execute(deposit.getId());
            } catch (Exception e) {
                // Isolate error: One failure should not affect others
                log.error("Error dispatching refund request for Deposit {}: {}", deposit.getId(), e.getMessage());
            }
        }

        log.info("[ASYNC WORKER] Completed refund dispatch for cancelled auction: {}", auctionId);

    }
}
