package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.enums.OrderStatus;
import com.example.vehicle_auction.domain.model.OrderModel;
import com.example.vehicle_auction.domain.repository.OrderRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Order;
import com.example.vehicle_auction.infrastructure.persistence.mapper.OrderEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final OrderEntityMapper mapper;

    @Override
    public OrderModel save(OrderModel orderModel) {
        Order entity;
        if (orderModel.getId() != null) {
            entity = jpaOrderRepository.findById(orderModel.getId()).orElseThrow();
            mapper.updateEntityFromModel(orderModel, entity);
        } else {
            entity = mapper.toEntity(orderModel);
        }
        return mapper.toDomain(jpaOrderRepository.save(entity));
    }

    @Override
    public Optional<OrderModel> findById(UUID id) {
        return jpaOrderRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<OrderModel> findByWinnerId(UUID winnerId, Pageable pageable) {
        return jpaOrderRepository.findByWinnerId(winnerId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<OrderModel> findByStatusAndPaymentDeadDateBefore(OrderStatus status, LocalDateTime currentTime) {
        return jpaOrderRepository.findByStatusAndPaymentDeadDateBefore(status, currentTime)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
