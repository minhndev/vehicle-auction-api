package com.example.vehicle_auction.application.usecase.deposit;

import com.example.vehicle_auction.application.dto.deposit.DepositRequest;
import com.example.vehicle_auction.application.dto.deposit.DepositResponse;
import com.example.vehicle_auction.application.dto.payment.PaymentRequest;
import com.example.vehicle_auction.application.dto.payment.PaymentResponse;
import com.example.vehicle_auction.application.mapper.DepositMapper;
import com.example.vehicle_auction.application.port.out.PaymentGatewayPort;
import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.model.DepositModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.domain.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateDepositUseCase {

    private final AuctionRepository auctionRepository;
    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;

    @Transactional
    public DepositResponse execute(DepositRequest request, UUID accountId) {
        AuctionModel auction = auctionRepository.findById(request.auctionId())
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

        if (depositRepository.hasPaidDeposit(request.auctionId(), accountId)) {
            throw new AppException(ErrorCode.DEPOSIT_ALREADY_PAID);
        }

        DepositModel deposit = DepositModel.builder()
                .accountId(accountId)
                .auctionId(auction.getId())
                .amount(auction.getDepositAmount())
                .status(DepositStatus.PENDING)
                .paymentMethod("VNPAY")
                .build();

        DepositModel savedDeposit = depositRepository.save(deposit);

        log.info("Account {} created PENDING deposit for Auction {}", accountId, auction.getId());

        return depositMapper.toResponse(savedDeposit);
    }
}
