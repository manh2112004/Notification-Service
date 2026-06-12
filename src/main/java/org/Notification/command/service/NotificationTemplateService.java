package org.Notification.command.service;

import org.Notification.command.data.NotificationTemplate;
import org.Notification.command.model.request.CreateNotificationTemplateRequest;
import org.Notification.command.model.request.UpdateNotificationTemplateRequest;

import java.util.concurrent.CompletableFuture;

public interface NotificationTemplateService {
    CompletableFuture<NotificationTemplate> createTemplate(CreateNotificationTemplateRequest request);
    CompletableFuture<NotificationTemplate> updateTemplate(String id, UpdateNotificationTemplateRequest request);
    CompletableFuture<Void> deleteTemplate(String id);
}
