package com.example.vehicle_auction.application.usecase.auction;

import com.example.vehicle_auction.application.dto.auction.AuctionRequest;
import com.example.vehicle_auction.application.dto.auction.AuctionResponse;
import com.example.vehicle_auction.application.mapper.AuctionMapper;
import com.example.vehicle_auction.infrastructure.persistence.entity.Auction;
import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaAuctionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreateAuctionUseCase {

    private final JpaAuctionRepository auctionRepository;
    private final JpaProductRepository productRepository;
    private final AuctionMapper auctionMapper;

    public AuctionResponse execute(AuctionRequest request){
        log.info("Starting to create new auction for product ID: {}", request.productId());

        // validate logic time
        if(request.endTime().isBefore(request.startTime()) || request.endTime().isEqual(request.startTime())){
            throw new AppException(ErrorCode.INVALID_AUCTION_TIME);
        }

        // Validate product existence
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Check if product is already in an active auction
        if (product.getStatus() == ProductStatus.PENDING || product.getStatus() == ProductStatus.IN_AUCTION || product.getStatus() == ProductStatus.SOLD) {
            log.warn("Product with ID {} is not approved for auction", request.productId());
            throw new AppException(ErrorCode.PRODUCT_NOT_APPROVED);
        }

        // Check for overlapping auctions
        boolean isOverlapping = auctionRepository.existsByProductIdAndStatusIn(
                product.getId(),
                List.of(AuctionStatus.UPCOMING, AuctionStatus.ACTIVE));
        if (isOverlapping) {
            throw new AppException(ErrorCode.AUCTION_OVERLAPS);
        }

        // Map request to entity
        Auction auction = auctionMapper.toEntity(request);
        auction.setProduct(product);

        // Set initial auction status and price
        auction.setCurrentPrice(request.startPrice());
        auction.setStatus(AuctionStatus.UPCOMING);

        // Update product status to IN_AUCTION
        product.setStatus(ProductStatus.IN_AUCTION);
        productRepository.save(product);

        // Save auction to database
        Auction savedAuction = auctionRepository.save(auction);
        log.info("Successfully created auction with ID: {} for product ID: {}", savedAuction.getId(), product.getId());

        return auctionMapper.toResponse(savedAuction);
    }
}
