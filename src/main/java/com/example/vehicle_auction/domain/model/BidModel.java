package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.BidStatus;
import com.example.vehicle_auction.domain.model.base.AuditModel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@SuperBuilder
public class BidModel extends AuditModel {
    private UUID auctionId;
    private UUID bidderId;
    private BigDecimal amount;
    private BidStatus status;
}
