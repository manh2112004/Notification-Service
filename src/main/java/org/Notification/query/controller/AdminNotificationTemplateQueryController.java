package org.Notification.query.controller;

import org.Notification.command.data.NotificationTemplate;
import org.Notification.command.data.NotificationType;
import org.Notification.query.model.response.NotificationTemplatePageResponse;
import org.Notification.query.service.NotificationTemplateQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/admin/notification-templates")
public class AdminNotificationTemplateQueryController {

    @Autowired
    private NotificationTemplateQueryService notificationTemplateQueryService;

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public CompletableFuture<NotificationTemplatePageResponse> getTemplates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String templateCode,
            @RequestParam(required = false) NotificationType type,
            @RequestParam(required = false) Boolean isActive
    ) {
        return notificationTemplateQueryService.getTemplates(page, size, templateCode, type, isActive);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public CompletableFuture<NotificationTemplate> getTemplateById(@PathVariable String id) {
        return notificationTemplateQueryService.getTemplateById(id);
    }
}
