package org.Notification.query.model.response;

import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
public class NotificationDeliveryLogPageResponse extends PageResponse<NotificationDeliveryLogResponse> {
    public NotificationDeliveryLogPageResponse(List<NotificationDeliveryLogResponse> content, int page, int size, long totalElements, int totalPages) {
        super(content, page, size, totalElements, totalPages);
    }
}
