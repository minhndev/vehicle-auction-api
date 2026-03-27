package com.example.vehicle_auction.application.usecase.deposit;

import com.example.vehicle_auction.application.dto.payment.RefundRequest;
import com.example.vehicle_auction.application.port.out.PaymentGatewayPort;
import com.example.vehicle_auction.application.port.out.TransactionRepositoryPort;
import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.enums.PaymentStatus;
import com.example.vehicle_auction.domain.model.DepositModel;
import com.example.vehicle_auction.domain.model.TransactionModel;
import com.example.vehicle_auction.domain.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundDepositUseCase {

    private final DepositRepository depositRepository;
    private final PaymentGatewayPort paymentGateway;
    private final TransactionRepositoryPort transactionRepository;

    @Transactional
    public void execute(UUID depositId) {
        log.info("Starting automatic deposit refund process for all users in Auction ID: {}", depositId);

        DepositModel deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Deposit"));

        if (deposit.getStatus() != DepositStatus.PAID && deposit.getStatus() != DepositStatus.REFUND_FAILED) {
            log.warn("Deposit {} không hợp lệ để hoàn tiền. Status: {}", depositId, deposit.getStatus());
            return;
        }

        String vnpRequestId = "REFUND-" + deposit.getId().toString().substring(0, 8) + "-" + UUID.randomUUID().toString().substring(0, 8);


        Optional<TransactionModel> existingSuccessRefund = transactionRepository.findByReferenceIdAndTargetTypeAndStatus(
                deposit.getId(),
                "REFUND_DEPOSIT",
                PaymentStatus.REFUNDED
        );

        Optional<TransactionModel> existingPendingRefund = transactionRepository.findByReferenceIdAndTargetTypeAndStatus(
                deposit.getId(),
                "REFUND_DEPOSIT",
                PaymentStatus.PENDING_REFUND
        );

        if (existingSuccessRefund.isPresent() || existingPendingRefund.isPresent()) {
            log.warn("CẢNH BÁO: Deposit {} đã được hoàn tiền trước đó. Hủy yêu cầu để tránh double-refund!", depositId);
            return;
        }

        deposit.setStatus(DepositStatus.REFUND_PROCESSING);

        TransactionModel refundTx = new TransactionModel(
                UUID.randomUUID(),
                deposit.getAccountId(),
                deposit.getId(),
                "REFUND_DEPOSIT",
                vnpRequestId,
                deposit.getAmount().longValue(),
                PaymentStatus.PENDING_REFUND,
                LocalDateTime.now(),
                null
        );

        try {
            String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            RefundRequest refundRequest = new RefundRequest(
                    deposit.getTransactionReference(),
                    deposit.getAmount().longValue() * 100,
                    "02",
                    deposit.getPaymentDate(),
                    deposit.getGatewayTransactionNo(),
                    createDate,
                    "minhn.tech@gmail.com"
            );

            // GỌI API VNPay
            boolean isRefundSuccess = paymentGateway.refund(refundRequest);

            //Cập nhật kết quả dựa trên phản hồi của VNPay
            if (isRefundSuccess) {
                deposit.setStatus(DepositStatus.REFUNDED);
                refundTx.setStatus(PaymentStatus.REFUNDED);
                log.info("Hoàn tiền THÀNH CÔNG cho Deposit ID: {}", deposit.getId());
            } else {
                deposit.setStatus(DepositStatus.REFUND_FAILED);
                refundTx.setStatus(PaymentStatus.REFUND_FAILED);
                log.error("VNPay TỪ CHỐI hoàn tiền cho Deposit ID: {}", deposit.getId());
            }

        } catch (Exception e) {
            log.error("Lỗi hệ thống khi gọi VNPay cho Deposit ID {}: {}", deposit.getId(), e.getMessage(), e);
            deposit.setStatus(DepositStatus.REFUND_FAILED);
            refundTx.setStatus(PaymentStatus.REFUND_FAILED);
        }

        depositRepository.save(deposit);
        transactionRepository.save(refundTx);
    }
}
