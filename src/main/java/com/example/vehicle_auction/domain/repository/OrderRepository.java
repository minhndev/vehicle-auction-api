package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.OrderModel;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    OrderModel save(OrderModel orderModel);

    Optional<OrderModel> findById(UUID id);
}
