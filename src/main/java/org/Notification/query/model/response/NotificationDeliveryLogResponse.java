package org.Notification.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Notification.command.data.DeliveryStatus;
import org.Notification.command.data.NotificationChannel;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDeliveryLogResponse {
    private String id;
    private String notificationId;
    private NotificationChannel channel;
    private DeliveryStatus status;
    private String provider;
    private String errorMessage;
    private Integer retryCount;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}
