package org.Notification.query.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Notification.command.data.DeliveryStatus;
import org.Notification.command.data.NotificationChannel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAdminDeliveryLogsQuery {
    private int page;
    private int size;
    private String notificationId;
    private DeliveryStatus status;
    private NotificationChannel channel;
}
