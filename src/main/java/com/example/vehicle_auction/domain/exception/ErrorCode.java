package com.example.vehicle_auction.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // System errors
    CANNOT_DELETE_SYSTEM_ROLE("SYS_001", "sys.cannot.delete.role"),
    UNCATEGORIZED_EXCEPTION("SYS_999", "sys.uncategorized"),

    // Business errors
    CONFIRM_PASSWORD_INVALID("ACCOUNT_001", "confirm_password.invalid"),
    EMAIL_ALREADY_EXISTS("ACCOUNT_002", "email.already.exists"),
    ROLE_NOT_FOUND("ROLE_404", "role.not.found"),
    ROLE_ALREADY_EXISTS("ROLE_001", "role.already.exists"),
    AUCTION_NOT_FOUND("AUC_404", "auction.not.found"),
    UNAUTHORIZED("AUTH_401", "auth.unauthorized"),

    // Category and Product errors
    CATEGORY_NOT_FOUND("CAT_001", "category.not.found"),
    CATEGORY_ALREADY_EXISTS("CAT_003", "category.already.exists"),
    PRODUCT_NOT_FOUND("PRD_001", "product.not.found"),
    VIN_NUMBER_EXISTS("PRD_002", "product.vin.exists"),
    PRODUCT_NOT_PENDING("PRD_004", "product.not.pending"),

    // Auction and Bid errors
    PRODUCT_NOT_APPROVED("PRD_003", "product.not.approved"),
    INVALID_AUCTION_TIME("AUC_002", "auction.time.invalid"),
    AUCTION_OVERLAPS("AUC_003", "auction.overlaps"),
    AUCTION_NOT_ACTIVE("AUC_004", "auction.not.active"),
    DEPOSIT_REQUIRED("BID_001", "bid.deposit.required"),
    BID_AMOUNT_TOO_LOW("BID_002", "bid.amount.too.low"),
    ACCOUNT_UNAUTHORIZED("ACCOUNT_401", "account.unauthorized"),
    REFRESH_UNAUTHORIZED("REFRESH_TOKEN_401", "refresh_token.unauthorized");

    private final String code;
    private final String messageKey;
}
