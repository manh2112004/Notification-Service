package org.Notification.query.model.response;

import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
public class NotificationPageResponse extends PageResponse<NotificationResponse> {
    public NotificationPageResponse(List<NotificationResponse> content, int page, int size, long totalElements, int totalPages) {
        super(content, page, size, totalElements, totalPages);
    }
}
