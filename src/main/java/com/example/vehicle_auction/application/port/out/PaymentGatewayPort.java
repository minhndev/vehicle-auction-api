package com.example.vehicle_auction.application.port.out;

import com.example.vehicle_auction.application.dto.payment.PaymentRequest;
import com.example.vehicle_auction.application.dto.payment.PaymentResponse;
import com.example.vehicle_auction.application.dto.payment.RefundRequest;

import java.util.Map;

public interface PaymentGatewayPort {
    PaymentResponse createPaymentUrl(PaymentRequest req);

    boolean verifyCallback(Map<String, String> callbackParams);

    boolean refund(RefundRequest request);
}
