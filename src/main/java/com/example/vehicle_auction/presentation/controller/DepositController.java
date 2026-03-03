package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.deposit.DepositRequest;
import com.example.vehicle_auction.application.dto.deposit.DepositResponse;
import com.example.vehicle_auction.application.usecase.deposit.CreateDepositUseCase;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deposits")
@RequiredArgsConstructor
@Tag(name = "Deposit Management", description = "APIs for auction deposits")
public class DepositController {

    private final CreateDepositUseCase createDepositUseCase;

    @Operation(summary = "Pay deposit for an auction (Mock)")
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Yêu cầu phải đăng nhập
    public ResponseEntity<DepositResponse> payDeposit(
            @Valid @RequestBody DepositRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // Lấy UUID accountId của người đang đăng nhập
        DepositResponse response = createDepositUseCase.execute(request, userDetails.getAccount().getId());
        return ResponseEntity.ok(response);
    }
}
