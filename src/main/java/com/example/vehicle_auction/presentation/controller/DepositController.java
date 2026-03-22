package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.deposit.DepositRequest;
import com.example.vehicle_auction.application.dto.payment.PaymentResponse;
import com.example.vehicle_auction.application.usecase.deposit.CreateDepositUseCase;
import com.example.vehicle_auction.application.usecase.deposit.ForfeitDepositUseCase;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> payDeposit(
            @Valid @RequestBody DepositRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest httpServletRequest
    ) {
        String ipAddress = getClientIpAddress(httpServletRequest);

        PaymentResponse response = createDepositUseCase.execute(
                request,
                userDetails.getAccount().getId(),
                ipAddress
        );

        return ResponseEntity.ok(response);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xForwardedForHeader.split(",")[0].trim();
    }
}
