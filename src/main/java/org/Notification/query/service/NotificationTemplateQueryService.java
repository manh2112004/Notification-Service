package org.Notification.query.service;

import org.Notification.command.data.NotificationType;
import org.Notification.query.model.response.NotificationTemplatePageResponse;

import java.util.concurrent.CompletableFuture;

public interface NotificationTemplateQueryService {
    CompletableFuture<NotificationTemplatePageResponse> getTemplates(
            int page, int size, String templateCode, NotificationType type, Boolean isActive
    );
}
