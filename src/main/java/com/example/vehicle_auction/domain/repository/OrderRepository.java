package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.OrderModel;

public interface OrderRepository {
    OrderModel save(OrderModel orderModel);
}
