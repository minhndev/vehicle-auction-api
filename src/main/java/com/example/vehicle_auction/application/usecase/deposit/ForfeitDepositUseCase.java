package com.example.vehicle_auction.application.usecase.deposit;

import com.example.vehicle_auction.application.dto.deposit.DepositResponse;
import com.example.vehicle_auction.application.mapper.DepositMapper;
import com.example.vehicle_auction.domain.enums.DepositStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Deposit;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaDepositRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForfeitDepositUseCase {

    private final JpaDepositRepository depositRepository;
    private final DepositMapper depositMapper;

    public DepositResponse execute(UUID id){
        log.info("Starting to forfeit deposit with ID: {}", id);

        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPOSIT_NOT_FOUND));

        if (deposit.getStatus() != DepositStatus.PAID){
            log.warn("Cannot forfeit deposit with ID {} because it is not in PAID status. Current status: {}",
                    id, deposit.getStatus());
            throw new AppException(ErrorCode.DEPOSIT_CANNOT_FORFEIT);
        }

        deposit.setStatus(DepositStatus.FORFEITED);
        Deposit updatedDeposit = depositRepository.save(deposit);
        log.info("Deposit with ID: {} has been FORFEITED", updatedDeposit.getId());

        return depositMapper.toResponse(updatedDeposit);
    }
}
