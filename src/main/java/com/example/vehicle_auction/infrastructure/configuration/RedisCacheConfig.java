package com.example.vehicle_auction.infrastructure.configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration(){
        // Tạo một ObjectMapper custom
        ObjectMapper objectMapper = new ObjectMapper();
        // Jackson cách xử lý LocalDateTime của Java 8
        objectMapper.registerModule(new JavaTimeModule());
        // Đổi format thời gian thành chuỗi ISO (VD: "2023-10-25T10:00:00") thay vì mảng số
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Quan trọng: Đánh dấu kiểu Class vào JSON để lúc lấy ra Spring Boot biết ép kiểu về Record nào
        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).build(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        // 4. Bỏ cái ObjectMapper xịn xò này vào Serializer
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);


        return RedisCacheConfiguration.defaultCacheConfig()
                //Setting 10 minutes as the default cache expiration time for all cache entries.
                .entryTtl(Duration.ofMinutes(10))
                // Skip caching null values.
                .disableCachingNullValues()
                // Configure the serialization for keys to string using StringRedisSerializer
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // Configure the serialization for values to JSON using GenericJackson2JsonRedisSerializer
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer));
    }
}
