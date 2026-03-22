package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.payment.CreatePaymentCommand;
import com.example.vehicle_auction.application.dto.payment.IpnResponse;
import com.example.vehicle_auction.application.dto.payment.PaymentCreateRequest;
import com.example.vehicle_auction.application.dto.payment.PaymentResponse;
import com.example.vehicle_auction.application.usecase.payment.CreatePaymentUseCase;
import com.example.vehicle_auction.application.usecase.payment.ProcessPaymentIpnUseCase;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final CreatePaymentUseCase createPaymentUseCase;
    private final ProcessPaymentIpnUseCase processPaymentIpnUseCase;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    /**
     * Endpoint for the frontend to request a VNPay checkout URL.
     */
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody PaymentCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest httpRequest
    ) {
        String ipAddress = getClientIpAddress(httpRequest);

        CreatePaymentCommand command = new CreatePaymentCommand(
                userDetails.getAccount().getId(),
                request.referenceId(),
                request.targetType(),
                request.amount(),
                ipAddress
        );

        PaymentResponse response = createPaymentUseCase.execute(command);
        return ResponseEntity.ok(response);
    }

    /**
     * The IPN (Webhook) endpoint that VNPay servers call directly in the background.
     * This MUST return a specific JSON format (IpnResponse) and HTTP 200 OK.
     */
    @GetMapping("/vnpay-ipn")
    public ResponseEntity<IpnResponse> vnpayIpn(@RequestParam Map<String, String> params) {
        IpnResponse response = processPaymentIpnUseCase.execute(params);
        return ResponseEntity.ok(response);
    }

    /**
     * The Return URL endpoint where the user's browser is redirected after paying on VNPay.
     */
    @GetMapping("/vnpay-return")
    public ResponseEntity<String> vnpayReturn(@RequestParam Map<String, String> params) {
        String responseCode = params.get("vnp_ResponseCode");
        String redirectUrl;

        if ("00".equals(responseCode)) {
            redirectUrl = frontendUrl + "/payment/success?ref=" + params.get("vnp_TxnRef");
        } else {
            redirectUrl = frontendUrl + "/payment/failed?ref=" + params.get("vnp_TxnRef");
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xForwardedForHeader.split(",")[0];
    }
}
