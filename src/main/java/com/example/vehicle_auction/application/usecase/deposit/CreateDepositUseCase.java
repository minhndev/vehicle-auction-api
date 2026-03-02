package com.example.vehicle_auction.application.usecase.deposit;

import com.example.vehicle_auction.application.dto.deposit.DepositRequest;
import com.example.vehicle_auction.application.dto.deposit.DepositResponse;
import com.example.vehicle_auction.application.mapper.DepositMapper;
import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Auction;
import com.example.vehicle_auction.infrastructure.persistence.entity.Deposit;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaAuctionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaDepositRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateDepositUseCase {

    private final JpaAuctionRepository auctionRepository;
    private final JpaDepositRepository depositRepository;
    private final DepositMapper depositMapper;

    @Transactional
    public DepositResponse execute(DepositRequest request, UUID accountId) {
        // Kiểm tra phiên đấu giá tồn tại không
        Auction auction = auctionRepository.findById(request.auctionId())
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

        // Kiểm tra xem user này đã nộp cọc chưa
        boolean hasPaid = depositRepository.existsByAuctionIdAndAccountIdAndStatus(
                request.auctionId(), accountId, DepositStatus.PAID
        );
        if (hasPaid) {
            throw new AppException(ErrorCode.DEPOSIT_ALREADY_PAID);
        }

        // 3. Giả lập thanh toán thành công và tạo bản ghi Deposit
        Deposit deposit = Deposit.builder()
                .accountId(accountId)
                .auctionId(auction.getId())
                .amount(auction.getDepositAmount())
                .status(DepositStatus.PAID)
                .paymentMethod(request.paymentMethod() != null ? request.paymentMethod() : "MOCK_PAYMENT")
                .transactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();

        Deposit savedDeposit = depositRepository.save(deposit);
        log.info("Account {} successfully paid deposit for Auction {}", accountId, auction.getId());

        return depositMapper.toResponse(savedDeposit);
    }
}
