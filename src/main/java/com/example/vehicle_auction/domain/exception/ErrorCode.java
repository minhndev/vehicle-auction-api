package com.example.vehicle_auction.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // System errors
    CANNOT_DELETE_SYSTEM_ROLE("SYS_400", "sys.cannot.delete.role"),
    UNCATEGORIZED_EXCEPTION("SYS_500", "sys.uncategorized"),
    FORBIDDEN_EXCEPTION("SYS_403", "sys.forbidden"),
    DATA_CONFLICT("SYS_002", "sys.data.conflict"),

    // Business errors
    CONFIRM_PASSWORD_INVALID("ACCOUNT_400", "confirm_password.invalid"),
    EMAIL_ALREADY_EXISTS("ACCOUNT_400", "email.already.exists"),
    ROLE_NOT_FOUND("ROLE_404", "role.not.found"),
    ROLE_ALREADY_EXISTS("ROLE_001", "role.already.exists"),
    PERMISSION_NOT_FOUND("PERMISSION_404", "permission.not.found"),
    PERMISSION_ALREADY_EXISTS("PERMISSION_400", "permission.already.exists"),
    AUCTION_NOT_FOUND("AUC_404", "auction.not.found"),
    UNAUTHORIZED("AUTH_401", "auth.unauthorized"),

    NOTIFICATION_NOT_FOUND("NOTIFICATION_404", "notification.not.found"),

    ACCOUNT_INVALID_VERIFICATION_TOKEN("ACCOUNT_400", "account.invalid.verification_token"),
    ACCOUNT_ALREADY_VERIFIED("ACCOUNT_400", "account.already.verified"),

    USER_NOT_FOUND("USER_404", "user.not.found"),

    WATCHLIST_ALREADY_EXISTS("WATCHLIST_400", "watchlist.already.exists"),

    // Category and Product errors
    CATEGORY_NOT_FOUND("CAT_001", "category.not.found"),
    CATEGORY_ALREADY_EXISTS("CAT_003", "category.already.exists"),
    PRODUCT_NOT_FOUND("PRD_001", "product.not.found"),
    VIN_NUMBER_EXISTS("PRD_002", "product.vin.exists"),
    PRODUCT_NOT_PENDING("PRD_004", "product.not.pending"),
    PRODUCT_CANNOT_UPDATE("PRD_005", "product.cannot.update"),
    PRODUCT_CANNOT_DELETE("PRD_006", "product.cannot.delete"),

    // Auction and Bid errors
    PRODUCT_NOT_APPROVED("PRD_003", "product.not.approved"),
    INVALID_AUCTION_TIME("AUC_002", "auction.time.invalid"),
    AUCTION_OVERLAPS("AUC_003", "auction.overlaps"),
    AUCTION_NOT_ACTIVE("AUC_004", "auction.not.active"),
    AUCTION_CANNOT_CANCEL("AUC_005", "auction.cannot.cancel"),
    UNAUTHORIZED_ACTION("AUC_006", "auction.unauthorized.action"),

    DEPOSIT_REQUIRED("BID_001", "bid.deposit.required"),
    DEPOSIT_ALREADY_PAID("BID_001", "deposit.already.paid"),
    BID_AMOUNT_TOO_LOW("BID_002", "bid.amount.too.low"),
    ACCOUNT_UNAUTHORIZED("ACCOUNT_401", "account.unauthorized"),
    REFRESH_UNAUTHORIZED("REFRESH_TOKEN_401", "refresh_token.unauthorized"),

    // Deposit
    DEPOSIT_NOT_FOUND("DEP_001", "deposit.not.found"),
    DEPOSIT_CANNOT_FORFEIT("DEP_002", "deposit.cannot.forfeit"),

    // Order
    ORDER_NOT_FOUND("ORD_001", "order.not.found"),
    ORDER_CANNOT_BE_PAID("ORD_002", "order.cannot.be.paid"),
    ORDER_PAYMENT_EXPIRED("ORD_003", "order.payment.expired"),
    ORDER_CANNOT_BE_UPDATED("ORD_004", "order.cannot.be.updated"),
    SHIPPING_INFO_REQUIRED("ORD_005", "order.shipping_info.required"),


    // File
    FILE_UPLOAD_FAILED("FILE_001", "file.upload.failed"),
    FILE_TOO_LARGE("FILE_002", "file.too.large"),;

    private final String code;
    private final String messageKey;
}
