package com.example.vehicle_auction.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class AuctionFinishedEvent extends ApplicationEvent {
    private final UUID auctionId;
    private final UUID winnerId;

    public AuctionFinishedEvent(Object source, UUID auctionId, UUID winnerId) {
        super(source);
        this.auctionId = auctionId;
        this.winnerId = winnerId;
    }
}
