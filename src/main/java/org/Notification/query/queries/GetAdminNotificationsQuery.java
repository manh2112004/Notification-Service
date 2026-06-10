package org.Notification.query.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Notification.command.data.NotificationChannel;
import org.Notification.command.data.NotificationStatus;
import org.Notification.command.data.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAdminNotificationsQuery {
    private int page;
    private int size;
    private String receiverId;
    private Boolean isRead;
    private NotificationType type;
    private NotificationStatus status;
    private NotificationChannel channel;
}
