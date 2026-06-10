package org.Notification.query.model.response;

import lombok.*;
import org.Notification.command.data.NotificationChannel;
import org.Notification.command.data.NotificationStatus;
import org.Notification.command.data.NotificationType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private String id;
    private String receiverId;
    private String title;
    private String content;
    private NotificationType type;
    private NotificationChannel channel;
    private NotificationStatus status;
    private String referenceId;
    private String referenceType;
    private Boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
