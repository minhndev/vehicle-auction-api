package com.example.vehicle_auction.application.usecase.auction;

import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.enums.OrderStatus;
import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.domain.event.AuctionFinishedEvent;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloseEndedAuctionsUseCase {

    private final AuctionRepository auctionRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute() {
        LocalDateTime now = LocalDateTime.now();
        int BATCH_SIZE = 100;
        Pageable pageable = PageRequest.of(0, BATCH_SIZE);
        Page<AuctionModel> page;

        do {
            page = auctionRepository.findAuctionsToClose(AuctionStatus.ACTIVE, now, pageable);
            List<AuctionModel> auctions = page.getContent();

            if (auctions.isEmpty()) {
                break;
            }

            for (AuctionModel auction : auctions) {
                ProductModel product = productRepository.findById(auction.getProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                if (auction.getWinnerId() != null) {
                    auction.setStatus(AuctionStatus.COMPLETED);
                    product.setStatus(ProductStatus.SOLD);

                    OrderModel order = OrderModel.builder()
                            .auctionId(auction.getId())
                            .winnerId(auction.getWinnerId())
                            .totalAmount(auction.getCurrentPrice())
                            .remainingAmount(auction.getCurrentPrice().subtract(auction.getDepositAmount()))
                            .status(OrderStatus.PENDING_PAYMENT)
                            .paymentDeadDate(now.plusDays(3))
                            .build();
                    orderRepository.save(order);
                    log.info("Auction {} COMPLETED. Order created for Winner {}", auction.getId(), auction.getWinnerId());

                    eventPublisher.publishEvent(new AuctionFinishedEvent(this, auction.getId(), auction.getWinnerId()));

                } else {
                    auction.setStatus(AuctionStatus.FAILED);
                    product.setStatus(ProductStatus.APPROVED);
                    log.info("Auction {} FAILED due to no bids.", auction.getId());

                    eventPublisher.publishEvent(new AuctionFinishedEvent(this, auction.getId(), null));
                }

            }
            auctionRepository.saveAll(auctions);

        } while (page.hasNext());

        log.info("Finished processing ended auctions.");
    }
}
