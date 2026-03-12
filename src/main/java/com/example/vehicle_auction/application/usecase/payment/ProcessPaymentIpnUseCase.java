package com.example.vehicle_auction.application.usecase.payment;

import com.example.vehicle_auction.application.dto.payment.IpnResponse;
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

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessPaymentIpnUseCase {
    private final PaymentGatewayPort paymentGatewayPort;
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final DepositRepository depositRepository;

    @Transactional
    public IpnResponse execute(Map<String, String> vnpayParams) {
        if (!paymentGatewayPort.verifyCallback(vnpayParams)) {
            return new IpnResponse("97", "Invalid Checksum");
        }

        String txnRef = vnpayParams.get("vnp_TxnRef");
        String responseCode = vnpayParams.get("vnp_ResponseCode");

        Optional<TransactionModel> txOptional = transactionRepositoryPort.findByGatewayReference(txnRef);
        if (txOptional.isEmpty()) {
            return new IpnResponse("01", "Order not found");
        }

        TransactionModel transaction = txOptional.get();

        if (transaction.getStatus() != PaymentStatus.PENDING) {
            return new IpnResponse("02", "Order already confirmed");
        }

        long vnpAmount = Long.parseLong(vnpayParams.get("vnp_Amount")) / 100;
        if (transaction.getAmount() != vnpAmount) {
            return new IpnResponse("04", "Invalid amount");
        }

        if ("00".equals(responseCode)) {
            transaction.markAsSuccess();

            if ("DEPOSIT".equalsIgnoreCase(transaction.getTargetType())) {
                DepositModel deposit = depositRepository.findById(transaction.getReferenceId())
                        .orElseThrow(() -> new RuntimeException("Deposit not found"));

                deposit.setStatus(DepositStatus.PAID);
                deposit.setTransactionReference(txnRef);
                depositRepository.save(deposit);
            }

        } else {
            transaction.markAsFailed();

            if ("DEPOSIT".equalsIgnoreCase(transaction.getTargetType())) {
                DepositModel deposit = depositRepository.findById(transaction.getReferenceId())
                        .orElseThrow(() -> new RuntimeException("Deposit not found"));

                deposit.setStatus(DepositStatus.FAILED);
                depositRepository.save(deposit);
            }
        }

        transactionRepositoryPort.save(transaction);
        return new IpnResponse("00", "Confirm Success");
    }
}
