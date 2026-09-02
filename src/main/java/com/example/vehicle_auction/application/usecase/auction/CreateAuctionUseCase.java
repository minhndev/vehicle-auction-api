package com.example.vehicle_auction.application.usecase.auction;

import com.example.vehicle_auction.application.dto.auction.AuctionRequest;
import com.example.vehicle_auction.application.dto.auction.AuctionResponse;
import com.example.vehicle_auction.application.mapper.AuctionMapper;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.model.ProductModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.domain.repository.ProductRepository;
import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.configuration.RabbitMQConfig;
import com.example.vehicle_auction.infrastructure.messaging.RabbitMQProducer;
import com.example.vehicle_auction.infrastructure.messaging.dto.AuctionLifecycleMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreateAuctionUseCase {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final AuctionMapper auctionMapper;
    private final RabbitMQProducer rabbitMQProducer;

    public AuctionResponse execute(AuctionRequest request){
        log.info("Starting to create new auction for product ID: {}", request.productId());

        if(request.endTime().isBefore(request.startTime()) || request.endTime().isEqual(request.startTime())){
            throw new AppException(ErrorCode.INVALID_AUCTION_TIME);
        }

        ProductModel product = productRepository.findById(request.productId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus() == ProductStatus.PENDING || product.getStatus() == ProductStatus.IN_AUCTION || product.getStatus() == ProductStatus.SOLD) {
            log.warn("Product with ID {} is not approved for auction", request.productId());
            throw new AppException(ErrorCode.PRODUCT_NOT_APPROVED);
        }

        boolean isOverlapping = auctionRepository.existsByProductIdAndStatusIn(
                product.getId(),
                List.of(AuctionStatus.UPCOMING, AuctionStatus.ACTIVE));
        if (isOverlapping) {
            throw new AppException(ErrorCode.AUCTION_OVERLAPS);
        }

        AuctionModel auction = auctionMapper.toDomain(request);
        auction.setCurrentPrice(request.startPrice());
        auction.setStatus(AuctionStatus.UPCOMING);


        product.setStatus(ProductStatus.IN_AUCTION);
        String productName = product.getName();

        productRepository.save(product);

        AuctionModel savedAuction = auctionRepository.save(auction);
        log.info("Successfully created auction with ID: {} for product ID: {}", savedAuction.getId(), product.getId());

        // Schedule START and END via RabbitMQ
        scheduleAuctionLifecycle(savedAuction);

        return auctionMapper.toResponse(savedAuction, productName);
    }

    private void scheduleAuctionLifecycle(AuctionModel auction) {
        LocalDateTime now = LocalDateTime.now();
        
        // 1. Schedule START
        long startDelay = Duration.between(now, auction.getStartTime()).toMillis();
        if (startDelay < 0) startDelay = 0; // Start immediately if time passed
        
        rabbitMQProducer.sendDelayedMessage(
                RabbitMQConfig.RK_AUCTION_LIFECYCLE,
                new AuctionLifecycleMessage(auction.getId(), "START"),
                startDelay
        );

        // 2. Schedule END
        long endDelay = Duration.between(now, auction.getEndTime()).toMillis();
        if (endDelay > 0) {
            rabbitMQProducer.sendDelayedMessage(
                    RabbitMQConfig.RK_AUCTION_LIFECYCLE,
                    new AuctionLifecycleMessage(auction.getId(), "END"),
                    endDelay
            );
        }
        
        log.info("Scheduled START (delay {}ms) and END (delay {}ms) messages for Auction {}", 
                startDelay, endDelay, auction.getId());
    }
}
