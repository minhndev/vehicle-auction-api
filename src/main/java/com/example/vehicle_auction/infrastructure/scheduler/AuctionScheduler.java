package com.example.vehicle_auction.infrastructure.scheduler;

import com.example.vehicle_auction.application.usecase.auction.CloseEndedAuctionsUseCase;
import com.example.vehicle_auction.application.usecase.auction.OpenScheduledAuctionsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionScheduler {

    private final OpenScheduledAuctionsUseCase openScheduledAuctionsUseCase;
    private final CloseEndedAuctionsUseCase closeEndedAuctionsUseCase;

    @Scheduled(fixedRate = 10000)
    public void processAuctionLifecycle() {
        log.debug("Running Auction Lifecycle Scheduler...");

        openScheduledAuctionsUseCase.execute();

        closeEndedAuctionsUseCase.execute();
    }
}
