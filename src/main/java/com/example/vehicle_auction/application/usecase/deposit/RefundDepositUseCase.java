package com.example.vehicle_auction.application.usecase.deposit;

import com.example.vehicle_auction.application.dto.deposit.DepositResponse;
import com.example.vehicle_auction.application.mapper.DepositMapper;
import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.DepositModel;
import com.example.vehicle_auction.domain.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundDepositUseCase {

    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;

    @Transactional
    public DepositResponse execute(UUID depositId) {
        log.info("Bắt đầu xử lý hoàn cọc cho Deposit ID: {}", depositId);

        // Tìm Deposit
        DepositModel deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Deposit not found"));
        // Thay UNCATEGORIZED_EXCEPTION bằng DEPOSIT_NOT_FOUND nếu bạn đã thêm vào enum

        // Chỉ được hoàn tiền nếu đang ở trạng thái PAID
        if (deposit.getStatus() != DepositStatus.PAID) {
            log.warn("Không thể hoàn cọc cho Deposit ID {} vì trạng thái hiện tại là {}", depositId, deposit.getStatus());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Chỉ có thể hoàn cọc cho giao dịch đã thanh toán (PAID)");
        }

        // (Mock) API Cổng thanh toán để hoàn tiền thật ở đây
        // paymentGateway.refund(deposit.getTransactionReference(), deposit.getAmount());

        deposit.setStatus(DepositStatus.REFUNDED);
        DepositModel savedDeposit = depositRepository.save(deposit);

        log.info("Đã hoàn cọc thành công cho Deposit ID: {}", depositId);

        return depositMapper.toResponse(savedDeposit);
    }
}
