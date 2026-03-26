package com.example.vehicle_auction.application.listener;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.event.AuctionFinishedEvent;
import com.example.vehicle_auction.domain.model.DepositModel;
import com.example.vehicle_auction.domain.repository.DepositRepository;
import com.example.vehicle_auction.infrastructure.configuration.RabbitMQConfig;
import com.example.vehicle_auction.infrastructure.messaging.RabbitMQProducer;
import com.example.vehicle_auction.infrastructure.messaging.dto.RefundMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionEventListener {

    private final DepositRepository depositRepository;
    private final RabbitMQProducer rabbitMQProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAuctionFinishedProcessRefunds(AuctionFinishedEvent event) {
        UUID auctionId = event.getAuctionId();
        UUID winnerId = event.getWinnerId();
        log.info("Starting refund scan for auction: {}", auctionId);

        List<DepositModel> allPaidDeposits = depositRepository.findByAuctionIdAndStatus(auctionId, DepositStatus.PAID);

        for (DepositModel deposit : allPaidDeposits) {
            if (deposit.getAccountId().equals(winnerId)) {
                log.info("Skipping refund for Winner ID: {}", winnerId);
                continue;
            }

            try {
                RefundMessage refundMessage = new RefundMessage(deposit.getId(), auctionId, deposit.getAccountId());
                rabbitMQProducer.sendMessage(RabbitMQConfig.RK_REFUND_PROCESS, refundMessage);
                log.info("Pushed refund command to RabbitMQ for Deposit: {}", deposit.getId());
            } catch (Exception e) {
                log.error("Error pushing refund request to RabbitMQ for Deposit {}: {}", deposit.getId(), e.getMessage());
            }
        }

        log.info("Completed refund dispatch for auction: {}", auctionId);
    }
}
