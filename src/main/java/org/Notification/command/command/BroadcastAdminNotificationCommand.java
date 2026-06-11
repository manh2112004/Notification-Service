package org.Notification.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Notification.command.data.NotificationChannel;
import org.Notification.command.data.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BroadcastAdminNotificationCommand {
    private String title;
    private String content;
    private NotificationType type;
    private NotificationChannel channel;
    private String token;
}
