package com.example.vehicle_auction.application.usecase.notification;

import com.example.vehicle_auction.domain.enums.NotificationType;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.NotificationModel;
import com.example.vehicle_auction.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationUseCase {
    private final NotificationRepository notificationRepository;

    @Transactional
    public void createNotification(UUID receiverAccountId,
                                   NotificationType type,
                                   String title,
                                   String content,
                                   UUID referenceId,
                                   String referenceType) {
        NotificationModel notification = new NotificationModel();
        notification.setId(UUID.randomUUID().toString());
        notification.setAccountId(receiverAccountId.toString());
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setReferenceId(referenceId.toString());
        notification.setReferenceType(referenceType);
        notification.setRead(false);

        notificationRepository.save(notification);
    }

    public List<NotificationModel> getMyNotifications(UUID myAccountId) {
        return notificationRepository.findByAccountId(myAccountId.toString());
    }

    public long getUnreadCount(UUID myAccountId) {
        return notificationRepository.countUnreadByAccountId(myAccountId.toString());
    }

    @Transactional
    public void markAsRead(UUID notificationId, UUID myAccountId) {
        NotificationModel notification = notificationRepository.findById(notificationId.toString())
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getAccountId().equals(myAccountId)) {
            throw new AppException(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        notification.markAsRead();
        notificationRepository.save(notification);
    }
}