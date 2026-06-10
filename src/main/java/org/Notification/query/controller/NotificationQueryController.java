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
import org.Notification.query.queries.GetUnreadNotificationCountQuery;
import org.Notification.query.queries.GetNotificationPreferenceQuery;
import org.Notification.query.model.response.NotificationPreferenceResponse;
import org.Notification.query.queries.GetAdminNotificationsQuery;
import org.Notification.command.data.NotificationChannel;
import org.Notification.command.data.NotificationStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/unread-count")
    public CompletableFuture<Long> getUnreadCount(
            @AuthenticationPrincipal Jwt jwt
    ) {
        GetUnreadNotificationCountQuery query = GetUnreadNotificationCountQuery.builder()
                .receiverId(jwt.getSubject())
                .build();

        return queryGateway.query(
                query,
                ResponseTypes.instanceOf(Long.class)
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

    @GetMapping("/admin/notifications")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public CompletableFuture<org.Notification.query.model.response.NotificationPageResponse> getAdminNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String receiverId,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) NotificationType type,
            @RequestParam(required = false) NotificationStatus status,
            @RequestParam(required = false) NotificationChannel channel
    ) {
        GetAdminNotificationsQuery query = GetAdminNotificationsQuery.builder()
                .page(page)
                .size(size)
                .receiverId(receiverId)
                .isRead(isRead)
                .type(type)
                .status(status)
                .channel(channel)
                .build();

        return queryGateway.query(
                query,
                ResponseTypes.instanceOf(org.Notification.query.model.response.NotificationPageResponse.class)
        );
    }
}
