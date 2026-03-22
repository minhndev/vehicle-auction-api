package com.example.vehicle_auction.application.usecase.payment;

import com.example.vehicle_auction.application.dto.payment.CreatePaymentCommand;
import com.example.vehicle_auction.application.dto.payment.PaymentRequest;
import com.example.vehicle_auction.application.dto.payment.PaymentResponse;
import com.example.vehicle_auction.application.port.out.TransactionRepositoryPort;
import com.example.vehicle_auction.application.port.out.PaymentGatewayPort;
import com.example.vehicle_auction.domain.enums.PaymentStatus;
import com.example.vehicle_auction.domain.model.TransactionModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreatePaymentUseCase {
    private final PaymentGatewayPort paymentGatewayPort;
    private final TransactionRepositoryPort transactionRepositoryPort;

    @Transactional
    public PaymentResponse execute(CreatePaymentCommand command) {
        String vnpayTxnRef = "TXN-" + System.currentTimeMillis() + "-" + command.userId().toString().substring(0, 4);

        TransactionModel newTransaction = new TransactionModel(
                UUID.randomUUID(),
                command.userId(),
                command.referenceId(),
                command.targetType(),
                vnpayTxnRef,
                command.amount(),
                PaymentStatus.PENDING,
                LocalDateTime.now(),
                null

        );

        transactionRepositoryPort.save(newTransaction);

        String orderInfo = String.format("Payment for %s %s", command.targetType(), command.referenceId());

        PaymentRequest paymentRequest = new PaymentRequest(
                vnpayTxnRef,
                command.amount(),
                orderInfo,
                command.ipAddress()
        );

        return paymentGatewayPort.createPaymentUrl(paymentRequest);
    }
}
