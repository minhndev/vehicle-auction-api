package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.order.OrderResponse;
import com.example.vehicle_auction.application.dto.payment.PaymentResponse;
import com.example.vehicle_auction.application.usecase.order.GetOrderUseCase;
import com.example.vehicle_auction.application.usecase.order.PayOrderUseCase;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing post-auction orders")
public class OrderController {

    private final GetOrderUseCase getOrderUseCase;
    private final PayOrderUseCase payOrderUseCase;

    @Operation(summary = "Get order details", description = "Lấy thông tin chi tiết đơn hàng dành cho người thắng cuộc")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
         OrderResponse response = getOrderUseCase.execute(id, userDetails.getAccount().getId());
         return ResponseEntity.ok(response);
    }

    @Operation(summary = "Checkout order via VNPay", description = "Thanh toán phần tiền còn lại của đơn hàng qua VNPay")
    @PostMapping("/{id}/pay")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> payOrder(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest httpServletRequest
    ) {
        String ipAddress = getClientIpAddress(httpServletRequest);

        // Gọi sang VNPay tương tự như lúc đóng cọc
         PaymentResponse response = payOrderUseCase.execute(id, userDetails.getAccount().getId(), ipAddress);
         return ResponseEntity.ok(response);
    }

    // Hàm Helper lấy IP (Tái sử dụng từ DepositController)
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xForwardedForHeader.split(",")[0].trim();
    }
}
