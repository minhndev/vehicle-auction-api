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
    ACCOUNT_UNAUTHORIZED("ACCOUNT_401", "account.unauthorized"),
    REFRESH_UNAUTHORIZED("REFRESH_TOKEN_401", "refresh_token.unauthorized");

    private final String code;
    private final String messageKey;
}
