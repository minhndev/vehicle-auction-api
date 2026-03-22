package com.example.vehicle_auction.application.dto.payment;

public record IpnResponse(
        String RspCode,
        String Message
) { }
