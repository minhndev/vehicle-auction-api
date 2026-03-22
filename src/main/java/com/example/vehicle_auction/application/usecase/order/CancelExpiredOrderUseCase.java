package com.example.vehicle_auction.application.usecase.order;

import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.enums.OrderStatus;
import com.example.vehicle_auction.domain.model.DepositModel;
import com.example.vehicle_auction.domain.model.OrderModel;
import com.example.vehicle_auction.domain.repository.DepositRepository;
import com.example.vehicle_auction.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelExpiredOrderUseCase {

    private final OrderRepository orderRepository;
    private final DepositRepository depositRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execute(OrderModel order) {
        log.info("Starting cancellation process for expired Order ID: {}", order.getId());

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        Optional<DepositModel> depositOpt = depositRepository.findByAuctionIdAndAccountId(
                order.getAuctionId(),
                order.getWinnerId()
        );

        if (depositOpt.isPresent()) {
            DepositModel deposit = depositOpt.get();
            deposit.setStatus(DepositStatus.FORFEITED);
            depositRepository.save(deposit);
            log.info("Forfeited deposit ID: {} for the defaulting user", deposit.getId());
        } else {
            log.warn("Deposit not found for Order ID: {}", order.getId());
        }

        log.info("Successfully processed expired Order ID: {}", order.getId());
    }
}
