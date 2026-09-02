package com.example.vehicle_auction.presentation.advice;

import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.presentation.response.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiError> handleAppException(AppException ex, Locale locale) {
        ErrorCode errorCode = ex.getErrorCode();

        String message = resolveMessage(errorCode, ex.getArgs(), locale);

        ApiError apiError = ApiError.builder()
                .code(errorCode.getCode())
                .message(message)
                .details(List.of())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> messageSource.getMessage(fieldError, locale))
                .toList();

        ApiError apiError = ApiError.builder()
                .code("VAL_400")
                .message("Validation failed")
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex) {
        log.error("Internal Server Error: ", ex);

        ApiError apiError = ApiError.builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(resolveMessage(ErrorCode.UNCATEGORIZED_EXCEPTION, null, Locale.getDefault()))
                .details(List.of())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.internalServerError().body(apiError);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleOptimisticLockingFailureException(Exception ex, Locale locale) {
        log.error("Internal Server Error: ", ex);

        ApiError apiError = ApiError.builder()
                .code(ErrorCode.DATA_CONFLICT.getCode())
                .message(resolveMessage(ErrorCode.DATA_CONFLICT, null, locale))
                .details(List.of())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler({AccessDeniedException.class, BadCredentialsException.class})
    public ResponseEntity<ApiError> handleAccessDenied(Exception ex, Locale locale) {
        ApiError apiError = ApiError.builder()
                .code(ErrorCode.FORBIDDEN_EXCEPTION.getCode())
                .message(resolveMessage(ErrorCode.FORBIDDEN_EXCEPTION, null, locale))
                .details(List.of())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    private String resolveMessage(ErrorCode errorCode, Object[] args, Locale locale) {
        return messageSource.getMessage(errorCode.getMessageKey(), args, errorCode.getMessageKey(), locale);
    }


}
