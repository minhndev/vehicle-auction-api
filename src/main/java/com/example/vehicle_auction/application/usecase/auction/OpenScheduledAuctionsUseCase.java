package com.example.vehicle_auction.application.usecase.auction;

import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenScheduledAuctionsUseCase {

    private final AuctionRepository auctionRepository;

    public void execute() {
        LocalDateTime now = LocalDateTime.now();
        List<AuctionModel> auctionsToOpen = auctionRepository.findAuctionsToOpen(AuctionStatus.UPCOMING, now);

        if (!auctionsToOpen.isEmpty()) {
            auctionsToOpen.forEach(auction -> {
                auction.setStatus(AuctionStatus.ACTIVE);
                log.info("Auction with ID {} status changed to ACTIVE", auction.getId());
            });
            auctionRepository.saveAll(auctionsToOpen);
        }
    }

}
