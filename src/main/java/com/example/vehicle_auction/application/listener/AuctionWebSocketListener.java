package com.example.vehicle_auction.application.listener;

import com.example.vehicle_auction.application.dto.bid.AuctionBidRealtimeMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.lang.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionWebSocketListener implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(@NonNull Message message, @NonNull byte[] pattern) {
        try {
            String body = new String(message.getBody());
            AuctionBidRealtimeMessage payload = objectMapper.readValue(body, AuctionBidRealtimeMessage.class);
            String destination = "/topic/auctions/" + payload.auctionId();

            messagingTemplate.convertAndSend(destination, payload);
            log.info("Pushed auction update from Redis to WS destination={}", destination);
        } catch (Exception e) {
            log.error("Failed to push auction update from Redis Pub/Sub", e);
        }
    }
}
