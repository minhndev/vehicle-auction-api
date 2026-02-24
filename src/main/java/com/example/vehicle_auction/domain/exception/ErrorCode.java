package com.example.vehicle_auction.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // System errors
    UNCATEGORIZED_EXCEPTION("SYS_999", "Uncategorized error occurred."),

    // Business errors
    AUCTION_NOT_FOUND("AUC_404", "Auction does not exist."),
    UNAUTHORIZED("AUTH_401", "You do not have permission to perform this action.");

    private final String code;
    private final String message;
}
