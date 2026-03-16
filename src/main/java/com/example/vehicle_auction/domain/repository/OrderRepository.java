package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.enums.OrderStatus;
import com.example.vehicle_auction.domain.model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    OrderModel save(OrderModel orderModel);

    Optional<OrderModel> findById(UUID id);

    Page<OrderModel> findByWinnerId(UUID winnerId, Pageable pageable);

    List<OrderModel> findByStatusAndPaymentDeadDateBefore(OrderStatus status, LocalDateTime currentTime);
}
