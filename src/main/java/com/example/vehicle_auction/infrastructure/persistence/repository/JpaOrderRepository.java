package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.infrastructure.persistence.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, UUID> {
}