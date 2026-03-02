package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.BidStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class BidModel {
    private UUID id;
    private UUID auctionId;
    private UUID bidderId;
    private BigDecimal amount;
    private BidStatus status;
    private LocalDateTime createdAt;
}
