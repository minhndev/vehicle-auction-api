package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class DepositModel {

    private UUID id;
    private UUID accountId;
    private UUID auctionId;
    private BigDecimal amount;
    private DepositStatus status;
    private String paymentMethod;
    private String transactionReference;

    // Các trường Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
