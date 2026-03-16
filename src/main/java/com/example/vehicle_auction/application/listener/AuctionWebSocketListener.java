package com.example.vehicle_auction.application.listener;

import com.example.vehicle_auction.domain.event.BidPlacedEvent;
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
public class AuctionWebSocketListener {

    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBidPlacedEvent(BidPlacedEvent event) {
        log.info("Phát sóng giá mới {} cho Auction ID: {}", event.newPrice(), event.auctionId());

        try {
            String destination = "/topic/auctions/" + event.auctionId();

            AuctionUpdateMessage payload = new AuctionUpdateMessage(
                    event.auctionId(),
                    event.newPrice(),
                    "Có giá mới: " + event.newPrice()
            );

            messagingTemplate.convertAndSend(destination, payload);
            log.info("Đã cập nhật Real-time thành công tới kênh: {}", destination);

        } catch (Exception e) {
            log.error("Lỗi khi phát sóng WebSocket: {}", e.getMessage(), e);
        }
    }

    public record AuctionUpdateMessage(UUID auctionId, BigDecimal currentPrice, String message) {}
}
