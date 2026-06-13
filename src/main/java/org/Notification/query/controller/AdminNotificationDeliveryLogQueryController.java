package org.Notification.query.controller;

import org.Notification.command.data.DeliveryStatus;
import org.Notification.command.data.NotificationChannel;
import org.Notification.query.model.response.NotificationDeliveryLogPageResponse;
import org.Notification.query.service.NotificationDeliveryLogQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/admin/delivery-logs")
public class AdminNotificationDeliveryLogQueryController {

    @Autowired
    private NotificationDeliveryLogQueryService notificationDeliveryLogQueryService;

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public CompletableFuture<NotificationDeliveryLogPageResponse> getDeliveryLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String notificationId,
            @RequestParam(required = false) DeliveryStatus status,
            @RequestParam(required = false) NotificationChannel channel
    ) {
        return notificationDeliveryLogQueryService.getDeliveryLogs(page, size, notificationId, status, channel);
    }
}
