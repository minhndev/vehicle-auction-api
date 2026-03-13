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
                .setAllowedOriginPatterns("*") // Allow all origins (customizable if needed)
                .withSockJS(); // Use SockJS as fallback for browsers not supporting WebSocket
    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Channel for real-time notifications
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for messages from client to server
    }
}
