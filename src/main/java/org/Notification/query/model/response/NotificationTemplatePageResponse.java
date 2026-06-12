package org.Notification.query.model.response;

import lombok.NoArgsConstructor;
import org.Notification.command.data.NotificationTemplate;
import java.util.List;

@NoArgsConstructor
public class NotificationTemplatePageResponse extends PageResponse<NotificationTemplate> {
    public NotificationTemplatePageResponse(List<NotificationTemplate> content, int page, int size, long totalElements, int totalPages) {
        super(content, page, size, totalElements, totalPages);
    }
}
