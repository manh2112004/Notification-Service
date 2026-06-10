package org.Notification.query.controller;

import org.Notification.query.model.response.NotificationPreferenceResponse;
import org.Notification.query.queries.GetNotificationPreferenceQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/notification-preferences")
public class NotificationPreferenceQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    public CompletableFuture<NotificationPreferenceResponse> getPreference(
            @AuthenticationPrincipal Jwt jwt
    ) {
        GetNotificationPreferenceQuery query = GetNotificationPreferenceQuery.builder()
                .userId(jwt.getSubject())
                .build();

        return queryGateway.query(
                query,
                ResponseTypes.instanceOf(NotificationPreferenceResponse.class)
        );
    }
}
