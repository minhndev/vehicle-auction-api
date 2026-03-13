package com.example.vehicle_auction.application.usecase.deposit;

import com.example.vehicle_auction.application.dto.payment.RefundRequest;
import com.example.vehicle_auction.application.port.out.PaymentGatewayPort;
import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.model.DepositModel;
import com.example.vehicle_auction.domain.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundDepositUseCase {

    private final DepositRepository depositRepository;
    private final PaymentGatewayPort paymentGateway;

    @Transactional
    public void execute(UUID auctionId) {
        log.info("Starting automatic deposit refund process for all users in Auction ID: {}", auctionId);

        List<DepositModel> paidDeposits = depositRepository.findByAuctionIdAndStatus(auctionId, DepositStatus.PAID);

        if (paidDeposits.isEmpty()) {
            log.info("No PAID deposits found for Auction ID: {}", auctionId);
            return;
        }

        for (DepositModel deposit : paidDeposits) {
            try {
                RefundRequest refundRequest = new RefundRequest(
                        deposit.getTransactionReference(),
                        deposit.getAmount().longValue(),
                        "02",
                        "20260313111610", // Note: Ensure this date is fetched dynamically in production
                        "SYSTEM"
                );

                boolean isRefundSuccess = paymentGateway.refund(refundRequest);

                if (isRefundSuccess) {
                    deposit.setStatus(DepositStatus.REFUNDED);
                    depositRepository.save(deposit);
                    log.info("Successfully refunded via VNPay for Deposit ID: {}", deposit.getId());
                } else {
                    log.error("VNPay rejected the refund for Deposit ID: {}", deposit.getId());
                }
            } catch (Exception e) {
                log.error("Unexpected error during refund for Deposit ID {}: {}", deposit.getId(), e.getMessage(), e);
            }
        }

        log.info("Successfully completed refund process for Auction ID: {}", auctionId);
    }
}
