package com.example.vehicle_auction.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(org.springframework.web.socket.config.annotation.StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-auctions")
                .setAllowedOriginPatterns("*") // Cho phép tất cả các nguồn gốc (có thể tùy chỉnh nếu cần)
                .withSockJS(); // Sử dụng SockJS để hỗ trợ fallback cho trình duyệt không hỗ trợ WebSocket
    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Kênh để gửi thông báo real-time
        registry.setApplicationDestinationPrefixes("/app"); // Prefix cho các message từ client gửi lên server
    }
}
