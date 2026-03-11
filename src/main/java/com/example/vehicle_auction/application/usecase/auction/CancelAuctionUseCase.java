package com.example.vehicle_auction.application.usecase.auction;

import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.domain.event.AuctionCancelledEvent;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.model.ProductModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelAuctionUseCase {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(UUID auctionId, String cancelReason) {
        AuctionModel auction = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        if (auction.getStatus() == com.example.vehicle_auction.domain.enums.AuctionStatus.CANCELLED) {
            throw new AppException(ErrorCode.AUCTION_CANNOT_CANCEL);
        }

        auction.setStatus(AuctionStatus.CANCELLED);

        ProductModel product = productRepository.findById(auction.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setStatus(ProductStatus.REJECTED);

        productRepository.save(product);
        auctionRepository.save(auction);

        eventPublisher.publishEvent(new AuctionCancelledEvent(auctionId, cancelReason));
    }
}
