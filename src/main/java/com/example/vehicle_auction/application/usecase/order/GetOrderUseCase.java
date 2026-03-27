package com.example.vehicle_auction.application.usecase.order;

import com.example.vehicle_auction.application.dto.order.OrderResponse;
import com.example.vehicle_auction.application.mapper.OrderMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.model.OrderModel;
import com.example.vehicle_auction.domain.model.ProductModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.domain.repository.OrderRepository;
import com.example.vehicle_auction.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetOrderUseCase {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final AuctionRepository auctionRepository;


    public OrderResponse execute(UUID orderId, UUID accountId) {

        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getWinnerId().equals(accountId)) {
            log.warn("Account ID {} try to get Order ID {} they not own", accountId, orderId);
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        AuctionModel auction = auctionRepository.findById(order.getAuctionId())
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

        ProductModel product = productRepository.findById(auction.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        String productName = product.getName();

        return orderMapper.toResponse(order, productName);
    }
}
