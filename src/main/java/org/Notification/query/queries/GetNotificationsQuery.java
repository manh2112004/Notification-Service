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
public class GetNotificationsQuery {
    private String receiverId;
    private int page;
    private int size;
    private Boolean isRead;
    private NotificationType type;
}
