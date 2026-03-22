package com.example.vehicle_auction.presentation.advice;

import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.presentation.response.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(anyString(), any(), anyString(), any(Locale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(messageSource.getMessage(any(org.springframework.context.MessageSourceResolvable.class), any(Locale.class)))
                .thenReturn("validation");
        handler = new GlobalExceptionHandler(messageSource);
    }

    @Test
    void shouldReturnNotFoundStatusForNotFoundErrorCode() {
        ResponseEntity<ApiError> response = handler.handleAppException(
                new AppException(ErrorCode.AUCTION_NOT_FOUND),
                Locale.ENGLISH
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.AUCTION_NOT_FOUND.getCode());
    }

    @Test
    void shouldReturnConflictStatusForConflictErrorCode() {
        ResponseEntity<ApiError> response = handler.handleAppException(
                new AppException(ErrorCode.EMAIL_ALREADY_EXISTS),
                Locale.ENGLISH
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldReturnGoneStatusForExpiredOrderPayment() {
        ResponseEntity<ApiError> response = handler.handleAppException(
                new AppException(ErrorCode.ORDER_PAYMENT_EXPIRED),
                Locale.ENGLISH
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.GONE);
    }

    @Test
    void shouldReturnUnauthorizedForExpiredResetToken() {
        ResponseEntity<ApiError> response = handler.handleAppException(
                new AppException(ErrorCode.ACCOUNT_RESET_TOKEN_EXPIRED),
                Locale.ENGLISH
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}

