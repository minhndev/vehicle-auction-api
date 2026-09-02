package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.domain.enums.OrderStatus;
import com.example.vehicle_auction.infrastructure.persistence.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByStatusAndPaymentDeadDateBefore(OrderStatus status, LocalDateTime currentTime);

    Page<Order> findByWinnerId(UUID winnerId, Pageable pageable);
}