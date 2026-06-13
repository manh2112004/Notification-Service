package org.Notification.query.service;

import org.Notification.command.data.DeliveryStatus;
import org.Notification.command.data.NotificationChannel;
import org.Notification.query.model.response.NotificationDeliveryLogPageResponse;

import java.util.concurrent.CompletableFuture;

public interface NotificationDeliveryLogQueryService {
    CompletableFuture<NotificationDeliveryLogPageResponse> getDeliveryLogs(
            int page, int size, String notificationId, DeliveryStatus status, NotificationChannel channel
    );
}
