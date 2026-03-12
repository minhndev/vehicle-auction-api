package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.payment.CreatePaymentCommand;
import com.example.vehicle_auction.application.dto.payment.IpnResponse;
import com.example.vehicle_auction.application.dto.payment.PaymentResponse;
import com.example.vehicle_auction.application.usecase.payment.CreatePaymentUseCase;
import com.example.vehicle_auction.application.usecase.payment.ProcessPaymentIpnUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final CreatePaymentUseCase createPaymentUseCase;
    private final ProcessPaymentIpnUseCase processPaymentIpnUseCase;

    public record PaymentCreateRequest(
            UUID referenceId,
            String targetType, // e.g., "ORDER", "DEPOSIT"
            long amount
    ) {}

    /**
     * Endpoint for the frontend to request a VNPay checkout URL.
     */
    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody PaymentCreateRequest request,
            HttpServletRequest httpRequest
            // Note: If using Spring Security, inject the user context here:
            // @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // Extract the IP Address (Required by VNPay)
        String ipAddress = getClientIpAddress(httpRequest);

        // TODO: Replace with actual user ID from your security context
        UUID userId = UUID.randomUUID();

        CreatePaymentCommand command = new CreatePaymentCommand(
                userId,
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
        // Here, you would typically:
        // 1. Verify the signature again (using your PaymentGatewayPort).
        // 2. Read the vnp_ResponseCode to see if it was successful.
        // 3. Redirect the user to your frontend's Success or Failure page.
        //
        // Note: Do NOT update the database here. Rely strictly on the IPN webhook for database updates
        // to prevent duplicate processing or missed updates if the user closes their browser.

        String responseCode = params.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            return ResponseEntity.ok("Payment was successful! You can close this window.");
        } else {
            return ResponseEntity.ok("Payment failed or was canceled.");
        }
    }

    // Helper method to get the real IP address, even behind a load balancer or proxy
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xForwardedForHeader.split(",")[0];
    }
}
