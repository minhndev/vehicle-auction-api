package com.example.vehicle_auction.application.usecase.auction;

import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.enums.OrderStatus;
import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.infrastructure.persistence.entity.Auction;
import com.example.vehicle_auction.infrastructure.persistence.entity.Order;
import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaAuctionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaOrderRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final JpaAuctionRepository auctionRepository;
    private final JpaOrderRepository orderRepository;

    @Transactional
    public void execute() {
        LocalDateTime now = LocalDateTime.now();
        int BATCH_SIZE = 100;
        Pageable pageable = PageRequest.of(0, BATCH_SIZE);
        Page<Auction> page;

        do {
            page = auctionRepository.findAuctionsToClose(AuctionStatus.ACTIVE, now, pageable);
            List<Auction> auctions = page.getContent();

            if (auctions.isEmpty()) {
                break;
            }

            for (Auction auction : auctions) {
                Product product = auction.getProduct();

                if (auction.getWinnerId() != null) {
                    auction.setStatus(AuctionStatus.COMPLETED);
                    product.setStatus(ProductStatus.SOLD);

                    Order order = Order.builder()
                            .auctionId(auction.getId())
                            .winnerId(auction.getWinnerId())
                            .totalAmount(auction.getCurrentPrice())
                            .remainingAmount(auction.getCurrentPrice().subtract(auction.getDepositAmount()))
                            .status(OrderStatus.PENDING_PAYMENT)
                            .paymentDeadDate(now.plusDays(3))
                            .build();
                    orderRepository.save(order);
                    log.info("Auction {} COMPLETED. Order created for Winner {}", auction.getId(), auction.getWinnerId());

                } else {
                    auction.setStatus(AuctionStatus.FAILED);
                    product.setStatus(ProductStatus.APPROVED);
                    log.info("Auction {} FAILED due to no bids.", auction.getId());
                }

            }
            auctionRepository.saveAll(auctions);

        } while (page.hasNext());

        log.info("Finished processing ended auctions.");
    }
}
