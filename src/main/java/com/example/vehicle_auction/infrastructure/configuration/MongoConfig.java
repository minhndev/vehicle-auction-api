package com.example.vehicle_auction.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("!test")
@EnableMongoRepositories(
        basePackages = "com.example.vehicle_auction.infrastructure.persistence.repository.mongo"
)
public class MongoConfig {
}