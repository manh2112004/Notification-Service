package org.Notification.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Notification.command.data.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateNotificationTemplateCommand {
    private String id;
    private String templateCode;
    private String titleTemplate;
    private String contentTemplate;
    private NotificationType type;
    private Boolean isActive;
}
