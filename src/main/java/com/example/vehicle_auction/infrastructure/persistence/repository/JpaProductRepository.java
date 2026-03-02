package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByVinNumber(String vinNumber);
}