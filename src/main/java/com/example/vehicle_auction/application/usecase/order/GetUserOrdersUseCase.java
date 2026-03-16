package com.example.vehicle_auction.application.usecase.order;

import com.example.vehicle_auction.application.dto.order.OrderResponse;
import com.example.vehicle_auction.application.mapper.OrderMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.model.ProductModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.domain.repository.OrderRepository;
import com.example.vehicle_auction.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserOrdersUseCase {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<OrderResponse> execute(UUID accountId, Pageable pageable) {
        log.info("Fetching orders for account ID: {}", accountId);

        return orderRepository.findByWinnerId(accountId, pageable)
                .map(order -> {
                    AuctionModel auction = auctionRepository.findById(order.getAuctionId())
                            .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

                    ProductModel product = productRepository.findById(auction.getProductId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                    return orderMapper.toResponse(order, product.getName());
                });
    }
}
