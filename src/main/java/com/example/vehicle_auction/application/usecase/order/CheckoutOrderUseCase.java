package com.example.vehicle_auction.application.usecase.order;

import com.example.vehicle_auction.application.dto.order.CheckoutRequest;
import com.example.vehicle_auction.application.dto.order.OrderResponse;
import com.example.vehicle_auction.application.mapper.OrderMapper;
import com.example.vehicle_auction.domain.enums.OrderStatus;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final AuctionRepository auctionRepository;

    @Transactional
    public OrderResponse execute(UUID orderId, UUID accountId, CheckoutRequest request) {
        log.info("Cập nhật thông tin giao hàng cho Order ID: {}", orderId);

        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        AuctionModel auction = auctionRepository.findById(order.getAuctionId())
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

        ProductModel product = productRepository.findById(auction.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        String productName = product.getName();

        if (!order.getWinnerId().equals(accountId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_UPDATED);
        }

        order.setRecipientName(request.recipientName());
        order.setRecipientPhone(request.recipientPhone());
        order.setShippingAddress(request.shippingAddress());
        order.setShippingNote(request.shippingNote());

        OrderModel savedOrder = orderRepository.save(order);

        log.info("Cập nhật thông tin giao hàng thành công!");

        return orderMapper.toResponse(savedOrder, productName);
    }
}
