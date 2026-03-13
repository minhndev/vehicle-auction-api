package com.example.vehicle_auction.application.listener;

import com.example.vehicle_auction.application.usecase.notification.NotificationUseCase;
import com.example.vehicle_auction.domain.enums.NotificationType;
import com.example.vehicle_auction.domain.event.OutbidEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutbidNotificationListener {

    private final NotificationUseCase notificationUseCase;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOutbidEvent(OutbidEvent event) {
        log.info("Started sending notification to user {} due to outbid in auction {}",
                event.previousWinnerId(), event.auctionId());

        try {
            // 1. SAVE NOTIFICATION TO MONGODB
            String title = "You have been outbid!";
            String content = "Someone just bid " + event.newHighestAmount() + " for the auction you are participating in. Place a higher bid to reclaim the lead!";

            notificationUseCase.createNotification(
                    event.previousWinnerId(),
                    NotificationType.OUTBID, // Ensure this enum exists in NotificationType
                    title,
                    content,
                    event.auctionId(),
                    "AUCTION"
            );

            // 2. PUSH NOTIFICATION VIA WEBSOCKETS TO EVERYONE VIEWING THE AUCTION
            // URL Topic: /topic/auctions/{auctionId}
            String destination = "/topic/auctions/" + event.auctionId();

            OutbidMessageResponse payload = new OutbidMessageResponse(
                    event.auctionId(),
                    event.newHighestAmount(),
                    "Someone just placed a new bid: " + event.newHighestAmount()
            );

            messagingTemplate.convertAndSend(destination, payload);
            log.info("Successfully pushed real-time data to channel: {}", destination);

        } catch (Exception e) {
            log.error("Error processing outbid event: {}", e.getMessage(), e);
        }
    }

    // Lightweight DTO for sending via WebSockets
    public record OutbidMessageResponse(UUID auctionId, BigDecimal currentPrice, String message) {}

}
