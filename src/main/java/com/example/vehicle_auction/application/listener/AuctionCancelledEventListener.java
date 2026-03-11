package com.example.vehicle_auction.application.listener;

import com.example.vehicle_auction.application.usecase.deposit.RefundDepositUseCase;
import com.example.vehicle_auction.domain.event.AuctionCancelledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionCancelledEventListener {

    private final RefundDepositUseCase refundDepositUseCase;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAuctionCancelled(AuctionCancelledEvent event) {
        log.info("Handling AuctionCancelledEvent for auctionId: {}, reason: {}",
                event.auctionId(), event.cancelReason());

        try {

            refundDepositUseCase.execute(event.auctionId());
            Thread.sleep(3000);

            log.info("Refund successfully to all user in auction: {}", event.auctionId());

        } catch (Exception e) {
            log.error("Error  {}", event.auctionId(), e);
        }
    }
}
