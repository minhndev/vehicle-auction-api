package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.model.base.AuditModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class DepositModel extends AuditModel {

    private UUID accountId;
    private UUID auctionId;
    private BigDecimal amount;
    private DepositStatus status;
    private String paymentMethod;
    private String paymentDate;
    private String gatewayTransactionNo;
    private String transactionReference;

}
