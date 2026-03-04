package com.example.vehicle_auction.application.listener;

import com.example.vehicle_auction.domain.event.OutbidEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class OutbidNotificationListener {

    // Có thể inject NotificationRepository hoặc WebSocketService vào đây

    @Async // Chạy trên một Thread riêng biệt (Virtual Thread)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOutbidEvent(OutbidEvent event) {
        log.info("Bắt đầu gửi thông báo cho user {} vì bị vượt giá ở phiên {}",
                event.previousWinnerId(), event.auctionId());

        // Logic tạo Notification lưu xuống DB (bảng notifications)
        // Logic đẩy Real-time qua WebSocket (nếu có)
    }
}
