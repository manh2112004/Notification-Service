package org.Notification.command.service;

import org.Notification.command.data.NotificationTemplate;
import org.Notification.command.model.request.CreateNotificationTemplateRequest;

import java.util.concurrent.CompletableFuture;

public interface NotificationTemplateService {
    CompletableFuture<NotificationTemplate> createTemplate(CreateNotificationTemplateRequest request);
}
