package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.usecase.notification.NotificationUseCase;
import com.example.vehicle_auction.domain.model.NotificationModel;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "API for managing user notifications")
public class NotificationController {
    private final NotificationUseCase notificationUseCase;

    private UUID getCurrentAccountId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().
                getAuthentication()
                .getPrincipal();
        return userDetails.getAccount().getId();
    }

    @GetMapping
    @Operation(summary = "Get my notification list")
    public ResponseEntity<List<NotificationModel>> getMyNotifications() {
        return ResponseEntity.ok(notificationUseCase.getMyNotifications(getCurrentAccountId()));
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Count the number of unread notifications.")
    public ResponseEntity<Long> getUnreadCount() {
        return ResponseEntity.ok(notificationUseCase.getUnreadCount(getCurrentAccountId()));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark a notification as read.")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        notificationUseCase.markAsRead(id, getCurrentAccountId());
        return ResponseEntity.noContent().build();
    }
}
