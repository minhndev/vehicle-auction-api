package com.example.vehicle_auction.infrastructure.scheduler;

import com.example.vehicle_auction.application.usecase.order.CancelExpiredOrderUseCase;
import com.example.vehicle_auction.domain.enums.OrderStatus;
import com.example.vehicle_auction.domain.model.OrderModel;
import com.example.vehicle_auction.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderExpirationCronJob {

    private final OrderRepository orderRepository;
    private final CancelExpiredOrderUseCase cancelExpiredOrderUseCase;

    @Scheduled(cron = "0 */5 * * * *")
    public void scanAndCancelExpiredOrders() {
        log.info("Starting CronJob: Scanning for expired PENDING_PAYMENT orders...");

        List<OrderModel> expiredOrders = orderRepository.findByStatusAndPaymentDeadDateBefore(
                OrderStatus.PENDING_PAYMENT,
                LocalDateTime.now()
        );

        if (expiredOrders.isEmpty()) {
            log.info("No expired orders found.");
            return;
        }

        log.info("Found {} expired orders. Proceeding to cancel and forfeit deposits...", expiredOrders.size());

        for (OrderModel order : expiredOrders) {
            try {
                cancelExpiredOrderUseCase.execute(order);
            } catch (Exception e) {
                log.error("Error processing cancellation for Order ID: {}.", order.getId(), e);
            }
        }

        log.info("Completed order scanning process!");
    }
}
