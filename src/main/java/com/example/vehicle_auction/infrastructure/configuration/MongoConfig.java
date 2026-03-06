package com.example.vehicle_auction.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.example.vehicle_auction.infrastructure.persistence.repository.mongo"
)
public class MongoConfig {
}