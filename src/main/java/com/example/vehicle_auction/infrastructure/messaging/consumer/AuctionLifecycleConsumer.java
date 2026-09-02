package com.example.vehicle_auction.infrastructure.messaging.consumer;

import com.example.vehicle_auction.application.usecase.auction.CloseEndedAuctionsUseCase;
import com.example.vehicle_auction.application.usecase.auction.OpenScheduledAuctionsUseCase;
import com.example.vehicle_auction.infrastructure.configuration.RabbitMQConfig;
import com.example.vehicle_auction.infrastructure.messaging.dto.AuctionLifecycleMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionLifecycleConsumer {

    private final OpenScheduledAuctionsUseCase openScheduledAuctionsUseCase;
    private final CloseEndedAuctionsUseCase closeEndedAuctionsUseCase;

    @RabbitListener(queues = RabbitMQConfig.Q_AUCTION_LIFECYCLE)
    public void consumeLifecycleMessage(AuctionLifecycleMessage message) {
        log.info("Consuming auction lifecycle message: AuctionId={}, Action={}", message.auctionId(), message.action());

        try {
            if ("START".equalsIgnoreCase(message.action())) {
                openScheduledAuctionsUseCase.execute(message.auctionId());
            } else if ("END".equalsIgnoreCase(message.action())) {
                closeEndedAuctionsUseCase.execute(message.auctionId());
            } else {
                log.warn("Unknown lifecycle action: {}", message.action());
            }
        } catch (Exception e) {
            log.error("Failed to process auction lifecycle for {}. Error: {}", message.auctionId(), e.getMessage());
            // Retry will happen via RabbitMQ
            throw e;
        }
    }
}
