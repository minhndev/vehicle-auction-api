package com.example.vehicle_auction.application.usecase.order;

import com.example.vehicle_auction.application.dto.payment.CreatePaymentCommand;
import com.example.vehicle_auction.application.dto.payment.PaymentResponse;
import com.example.vehicle_auction.application.usecase.payment.CreatePaymentUseCase;
import com.example.vehicle_auction.domain.enums.OrderStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.OrderModel;
import com.example.vehicle_auction.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderUseCase {

    private final OrderRepository orderRepository;
    private final CreatePaymentUseCase createPaymentUseCase;

    @Transactional(noRollbackFor = AppException.class)
    public PaymentResponse execute(UUID orderId, UUID accountId, String ipAddress) {
        log.info("Started processing payment for Order ID: {} by Account ID: {}", orderId, accountId);

        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getWinnerId().equals(accountId)) {
            log.warn("Account ID {} attempted to pay for Order ID {} which they do not own", accountId, orderId);
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            log.warn("Order ID {} is not in PENDING_PAYMENT status. Current status: {}", orderId, order.getStatus());
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_PAID);
        }

        if (order.getPaymentDeadDate() != null && LocalDateTime.now().isAfter(order.getPaymentDeadDate())) {
            log.warn("Order ID {} exceeded the payment deadline at {}. Cancelling the order.", orderId, order.getPaymentDeadDate());

            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            throw new AppException(ErrorCode.ORDER_PAYMENT_EXPIRED);
        }

        if (order.getShippingAddress() == null || order.getShippingAddress().isEmpty()) {
            log.warn("Order ID {} has not updated shipping information!", orderId);
            throw new AppException(ErrorCode.SHIPPING_INFO_REQUIRED);
        }

        CreatePaymentCommand command = new CreatePaymentCommand(
                accountId,
                order.getId(),
                "ORDER",
                order.getRemainingAmount().longValue(),
                ipAddress
        );

        log.info("Successfully generated VNPay URL for Order ID: {}", orderId);
        return createPaymentUseCase.execute(command);
    }
}
