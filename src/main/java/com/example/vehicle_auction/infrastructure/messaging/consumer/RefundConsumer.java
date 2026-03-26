package com.example.vehicle_auction.infrastructure.messaging.consumer;

import com.example.vehicle_auction.application.usecase.deposit.RefundDepositUseCase;
import com.example.vehicle_auction.infrastructure.messaging.dto.RefundMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.example.vehicle_auction.infrastructure.configuration.RabbitMQConfig;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundConsumer {

    private final RefundDepositUseCase refundDepositUseCase;

    @RabbitListener(queues = RabbitMQConfig.Q_REFUND_PROCESS)
    public void consumeRefundMessage(RefundMessage message) {
        log.info("Consuming refund process for depositId: {}, Auction: {}", message.depositId(), message.auctionId());
        try {
            refundDepositUseCase.execute(message.depositId());
            log.info("Successfully processed refund for depositId: {}", message.depositId());
        } catch (Exception e) {
            log.error("Failed to process refund for depositId: {}. Error: {}", message.depositId(), e.getMessage());
            // RabbitMQ will retry
            throw e;
        }
    }
}
