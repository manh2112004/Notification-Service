package org.Notification.query.controller;

import org.Notification.query.model.response.NotificationResponse;
import org.Notification.query.queries.GetAdminNotificationByIdQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/admin/notifications")
public class AdminNotificationQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public CompletableFuture<NotificationResponse> getNotificationById(@PathVariable String id) {
        GetAdminNotificationByIdQuery query = GetAdminNotificationByIdQuery.builder()
                .id(id)
                .build();

        return queryGateway.query(
                query,
                ResponseTypes.instanceOf(NotificationResponse.class)
        );
    }
}
