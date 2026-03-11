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
        log.info("Bắt đầu gửi thông báo cho user {} vì bị vượt giá ở phiên {}",
                event.previousWinnerId(), event.auctionId());

        try {
            // 1. LƯU THÔNG BÁO VÀO MONGODB
            String title = "Bạn đã bị vượt giá!";
            String content = "Có người vừa trả giá " + event.newHighestAmount() + " cho phiên đấu giá mà bạn tham gia. Hãy đặt giá cao hơn để giành lại vị trí dẫn đầu!";

            notificationUseCase.createNotification(
                    event.previousWinnerId(),
                    NotificationType.OUTBID, // Nhớ đảm bảo enum này tồn tại trong NotificationType
                    title,
                    content,
                    event.auctionId(),
                    "AUCTION"
            );

            // 2. ĐẨY THÔNG BÁO QUA WEBSOCKETS CHO MỌI NGƯỜI ĐANG XEM PHIÊN ĐẤU GIÁ
            // URL Topic: /topic/auctions/{auctionId}
            String destination = "/topic/auctions/" + event.auctionId();

            OutbidMessageResponse payload = new OutbidMessageResponse(
                    event.auctionId(),
                    event.newHighestAmount(),
                    "Có người vừa đặt giá mới: " + event.newHighestAmount()
            );

            messagingTemplate.convertAndSend(destination, payload);
            log.info("Đã đẩy dữ liệu Real-time thành công tới kênh: {}", destination);

        } catch (Exception e) {
            log.error("Lỗi khi xử lý sự kiện vượt giá: {}", e.getMessage(), e);
        }
    }

    // DTO siêu nhỏ gọn dùng để gửi qua WebSockets
    public record OutbidMessageResponse(UUID auctionId, BigDecimal currentPrice, String message) {}

}
