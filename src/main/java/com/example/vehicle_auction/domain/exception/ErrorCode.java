package com.example.vehicle_auction.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // System errors
    CANNOT_DELETE_SYSTEM_ROLE("SYS_400", "sys.cannot.delete.role", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION("SYS_500", "sys.uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    FORBIDDEN_EXCEPTION("SYS_403", "sys.forbidden", HttpStatus.FORBIDDEN),
    DATA_CONFLICT("SYS_002", "sys.data.conflict", HttpStatus.CONFLICT),

    // Business errors
    CONFIRM_PASSWORD_INVALID("ACCOUNT_400", "confirm_password.invalid", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("ACCOUNT_400", "email.already.exists", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND("ROLE_404", "role.not.found", HttpStatus.NOT_FOUND),
    ROLE_ALREADY_EXISTS("ROLE_001", "role.already.exists", HttpStatus.CONFLICT),
    PERMISSION_NOT_FOUND("PERMISSION_404", "permission.not.found", HttpStatus.NOT_FOUND),
    PERMISSION_ALREADY_EXISTS("PERMISSION_400", "permission.already.exists", HttpStatus.CONFLICT),
    AUCTION_NOT_FOUND("AUC_404", "auction.not.found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("AUTH_401", "auth.unauthorized", HttpStatus.UNAUTHORIZED),

    NOTIFICATION_NOT_FOUND("NOTIFICATION_404", "notification.not.found", HttpStatus.NOT_FOUND),

    ACCOUNT_INVALID_VERIFICATION_TOKEN("ACCOUNT_400", "account.invalid.verification_token", HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_VERIFIED("ACCOUNT_400", "account.already.verified", HttpStatus.BAD_REQUEST),
    ACCOUNT_INVALID_RESET_TOKEN("ACCOUNT_400", "account.invalid.reset_token", HttpStatus.BAD_REQUEST),
    ACCOUNT_RESET_TOKEN_EXPIRED("ACCOUNT_401", "account.reset_token.expired", HttpStatus.UNAUTHORIZED),
    ACCOUNT_CONFIRM_PASSWORD_INVALID("ACCOUNT_400", "account.confirm_password.invalid", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_FOUND("ACCOUNT_404", "account.not.found", HttpStatus.BAD_REQUEST),
    ACCOUNT_VERIFICATION_TOKEN_EXPIRED("ACCOUNT_400", "account.verification_token.expired", HttpStatus.BAD_REQUEST),

    USER_NOT_FOUND("USER_404", "user.not.found", HttpStatus.NOT_FOUND),

    WATCHLIST_ALREADY_EXISTS("WATCHLIST_400", "watchlist.already.exists", HttpStatus.CONFLICT),

    // Category and Product errors
    CATEGORY_NOT_FOUND("CAT_001", "category.not.found", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS("CAT_003", "category.already.exists", HttpStatus.CONFLICT),
    PRODUCT_NOT_FOUND("PRD_001", "product.not.found", HttpStatus.NOT_FOUND),
    VIN_NUMBER_EXISTS("PRD_002", "product.vin.exists", HttpStatus.CONFLICT),
    PRODUCT_NOT_PENDING("PRD_004", "product.not.pending", HttpStatus.BAD_REQUEST),
    PRODUCT_CANNOT_UPDATE("PRD_005", "product.cannot.update", HttpStatus.BAD_REQUEST),
    PRODUCT_CANNOT_DELETE("PRD_006", "product.cannot.delete", HttpStatus.BAD_REQUEST),

    // Auction and Bid errors
    PRODUCT_NOT_APPROVED("PRD_003", "product.not.approved", HttpStatus.BAD_REQUEST),
    INVALID_AUCTION_TIME("AUC_002", "auction.time.invalid", HttpStatus.BAD_REQUEST),
    AUCTION_OVERLAPS("AUC_003", "auction.overlaps", HttpStatus.CONFLICT),
    AUCTION_UPCOMING("AUC_004", "auction.upcoming", HttpStatus.BAD_REQUEST),
    AUCTION_ENDED("AUC_005", "auction.ended", HttpStatus.BAD_REQUEST),
    AUCTION_SUSPENDED("AUC_006", "auction.suspended", HttpStatus.BAD_REQUEST),
    AUCTION_CANNOT_CANCEL("AUC_007", "auction.cannot.cancel", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACTION("AUC_008", "auction.unauthorized.action", HttpStatus.FORBIDDEN),

    DEPOSIT_REQUIRED("BID_001", "bid.deposit.required", HttpStatus.FORBIDDEN),
    DEPOSIT_ALREADY_PAID("BID_003", "deposit.already.paid", HttpStatus.CONFLICT),
    BID_AMOUNT_TOO_LOW("BID_002", "bid.amount.too.low", HttpStatus.BAD_REQUEST),
    ACCOUNT_UNAUTHORIZED("ACCOUNT_401", "account.unauthorized", HttpStatus.UNAUTHORIZED),
    REFRESH_UNAUTHORIZED("REFRESH_TOKEN_401", "refresh_token.unauthorized", HttpStatus.UNAUTHORIZED),

    // Deposit
    DEPOSIT_NOT_FOUND("DEP_001", "deposit.not.found", HttpStatus.NOT_FOUND),
    DEPOSIT_CANNOT_FORFEIT("DEP_002", "deposit.cannot.forfeit", HttpStatus.BAD_REQUEST),

    // Order
    ORDER_NOT_FOUND("ORD_001", "order.not.found", HttpStatus.NOT_FOUND),
    ORDER_CANNOT_BE_PAID("ORD_002", "order.cannot.be.paid", HttpStatus.BAD_REQUEST),
    ORDER_PAYMENT_EXPIRED("ORD_003", "order.payment.expired", HttpStatus.GONE),
    ORDER_CANNOT_BE_UPDATED("ORD_004", "order.cannot.be.updated", HttpStatus.BAD_REQUEST),
    SHIPPING_INFO_REQUIRED("ORD_005", "order.shipping_info.required", HttpStatus.BAD_REQUEST),

    CONTACT_NOT_FOUND("CONTACT_404", "contact.not.found", HttpStatus.NOT_FOUND),

    // File
    FILE_UPLOAD_FAILED("FILE_001", "file.upload.failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_TOO_LARGE("FILE_002", "file.too.large", HttpStatus.PAYLOAD_TOO_LARGE);

    private final String code;
    private final String messageKey;
    private final HttpStatus httpStatus;
}
