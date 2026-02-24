package com.example.vehicle_auction.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // System errors
    UNCATEGORIZED_EXCEPTION("SYS_999", "sys.uncategorized"),

    // Business errors
    ROLE_ALREADY_EXISTS("ROLE_001", "role.already.exists"),
    AUCTION_NOT_FOUND("AUC_404", "auction.not.found"),
    UNAUTHORIZED("AUTH_401", "auth.unauthorized");

    private final String code;
    private final String message;
}
