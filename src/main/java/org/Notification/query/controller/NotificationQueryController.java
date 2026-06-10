package org.Notification.query.controller;

import org.Notification.command.data.NotificationType;
import org.Notification.query.model.response.NotificationResponse;
import org.Notification.query.model.response.PageResponse;
import org.Notification.query.queries.GetNotificationsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.Notification.query.queries.GetNotificationByIdQuery;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    public CompletableFuture<org.Notification.query.model.response.NotificationPageResponse> getNotifications(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) NotificationType type
    ) {
        System.out.println(">>> Receiver ID from Token (Subject): " + jwt.getSubject());
        System.out.println(">>> All claims in Token: " + jwt.getClaims());
        GetNotificationsQuery query = GetNotificationsQuery.builder()
                .receiverId(jwt.getSubject())
                .page(page)
                .size(size)
                .isRead(isRead)
                .type(type)
                .build();

        return queryGateway.query(
                query,
                ResponseTypes.instanceOf(org.Notification.query.model.response.NotificationPageResponse.class)
        );
    }

    @GetMapping("/{id}")
    public CompletableFuture<NotificationResponse> getNotification(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id
    ) {
        GetNotificationByIdQuery query = GetNotificationByIdQuery.builder()
                .id(id)
                .receiverId(jwt.getSubject())
                .build();

        return queryGateway.query(
                query,
                ResponseTypes.instanceOf(NotificationResponse.class)
        );
    }
}
