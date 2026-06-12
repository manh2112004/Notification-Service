package org.Notification.command.controller;

import jakarta.validation.Valid;
import org.Notification.command.data.NotificationTemplate;
import org.Notification.command.model.request.CreateNotificationTemplateRequest;
import org.Notification.command.service.NotificationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/admin/notification-templates")
public class AdminNotificationTemplateCommandController {

    @Autowired
    private NotificationTemplateService notificationTemplateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public CompletableFuture<NotificationTemplate> createTemplate(@Valid @RequestBody CreateNotificationTemplateRequest request) {
        return notificationTemplateService.createTemplate(request);
    }
}
