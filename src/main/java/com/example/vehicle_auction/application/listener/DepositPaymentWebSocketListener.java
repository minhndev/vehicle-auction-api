package com.example.vehicle_auction.application.listener;

import com.example.vehicle_auction.domain.event.DepositPaymentProcessedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositPaymentWebSocketListener {
    private static final String DESTINATION = "/queue/deposits/payment-status";

    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDepositPaymentProcessed(DepositPaymentProcessedEvent event) {
        try {
            DepositPaymentStatusMessage payload = new DepositPaymentStatusMessage(
                    "DEPOSIT_PAYMENT_UPDATED",
                    event.auctionId(),
                    event.depositId(),
                    event.transactionRef(),
                    event.gatewayTransactionNo(),
                    event.vnpResponseCode(),
                    event.paymentStatus().name(),
                    event.depositStatus().name(),
                    event.occurredAt(),
                    "PAID".equals(event.depositStatus().name()) ? "Deposit paid successfully" : "Deposit payment failed"
            );

            messagingTemplate.convertAndSendToUser(event.accountId().toString(), DESTINATION, payload);
        } catch (Exception ex) {
            log.error("Failed to push deposit payment status via websocket for account {}", event.accountId(), ex);
        }
    }

    public record DepositPaymentStatusMessage(
            String type,
            UUID auctionId,
            UUID depositId,
            String transactionRef,
            String gatewayTransactionNo,
            String vnpResponseCode,
            String paymentStatus,
            String depositStatus,
            LocalDateTime occurredAt,
            String message
    ) {}
}

