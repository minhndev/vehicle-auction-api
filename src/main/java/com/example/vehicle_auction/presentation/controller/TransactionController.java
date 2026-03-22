package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.payment.TransactionResponse;
import com.example.vehicle_auction.application.usecase.payment.GetMyTransactionsUseCase;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "APIs for current user's payment transactions")
public class TransactionController {
    private final GetMyTransactionsUseCase getMyTransactionsUseCase;

    @Operation(summary = "Get my transactions")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<TransactionResponse>> getMyTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ParameterObject Pageable pageable
    ) {
        Page<TransactionResponse> response = getMyTransactionsUseCase.execute(userDetails.getAccount().getId(), pageable);
        return ResponseEntity.ok(response);
    }
}

