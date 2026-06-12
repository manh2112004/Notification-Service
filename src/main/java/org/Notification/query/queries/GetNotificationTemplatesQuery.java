package org.Notification.query.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Notification.command.data.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetNotificationTemplatesQuery {
    private int page;
    private int size;
    private String templateCode;
    private NotificationType type;
    private Boolean isActive;
}
