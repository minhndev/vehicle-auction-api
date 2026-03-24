package com.example.vehicle_auction.infrastructure.configuration;

import com.example.vehicle_auction.application.listener.AuctionWebSocketListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisPubSubConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private final AuctionWebSocketListener auctionWebSocketListener;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(auctionWebSocketListener, new PatternTopic("auction:*:events:bids"));
        return container;
    }
}

