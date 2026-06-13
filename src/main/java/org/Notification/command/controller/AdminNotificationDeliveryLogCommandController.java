package org.Notification.command.controller;

import org.Notification.command.command.RetryNotificationDeliveryLogCommand;
import org.Notification.command.data.NotificationDeliveryLog;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/admin/delivery-logs")
public class AdminNotificationDeliveryLogCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("/{id}/retry")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public CompletableFuture<NotificationDeliveryLog> retryDeliveryLog(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        RetryNotificationDeliveryLogCommand command = RetryNotificationDeliveryLogCommand.builder()
                .id(id)
                .token(jwt != null ? jwt.getTokenValue() : null)
                .build();

        return commandGateway.send(command);
    }
}
