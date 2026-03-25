package com.example.vehicle_auction.application.listener;

import com.example.vehicle_auction.application.dto.notification.NotificationResponse;
import com.example.vehicle_auction.application.usecase.notification.NotificationUseCase;
import com.example.vehicle_auction.domain.enums.NotificationType;
import com.example.vehicle_auction.domain.event.OutbidEvent;
import com.example.vehicle_auction.domain.model.NotificationModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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
            String title = "You have been outbid!";
            String content = "Someone just bid " + event.newHighestAmount() + " for the auction you are participating in. Place a higher bid to reclaim the lead!";

            NotificationModel savedNotification = notificationUseCase.createNotification(
                    event.previousWinnerId(),
                    NotificationType.OUTBID,
                    title,
                    content,
                    event.auctionId(),
                    "AUCTION"
            );

            log.info("Successfully save Outbid notification into DB");

            NotificationResponse payload = new NotificationResponse(
                    savedNotification.getId(),
                    savedNotification.getAccountId(),
                    savedNotification.getType(),
                    savedNotification.getTitle(),
                    savedNotification.getContent(),
                    savedNotification.getReferenceId(),
                    savedNotification.getReferenceType(),
                    savedNotification.isRead(),
                    savedNotification.getReadAt(),
                    savedNotification.getCreatedAt()
            );

            messagingTemplate.convertAndSendToUser(
                    event.previousWinnerId().toString(),
                    "/queue/notification",
                    payload
            );

            log.info("Successfully pushed WebSocket notification to destination: {}", event.previousWinnerId());
        } catch (Exception e) {
            log.error("Error processing outbid event: {}", e.getMessage(), e);
        }
    }


}
