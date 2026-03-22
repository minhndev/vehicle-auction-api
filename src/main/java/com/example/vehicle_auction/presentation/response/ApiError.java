package com.example.vehicle_auction.presentation.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApiError {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    @Builder.Default
    private List<String> details = List.of();
}
