package org.Notification.command.controller;

import jakarta.validation.Valid;
import org.Notification.command.command.CreateAdminNotificationCommand;
import org.Notification.command.data.Notification;
import org.Notification.command.model.request.AdminCreateNotificationRequest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/admin/notifications")
public class AdminNotificationCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public CompletableFuture<List<Notification>> createAdminNotifications(@Valid @RequestBody AdminCreateNotificationRequest request) {
        CreateAdminNotificationCommand command = CreateAdminNotificationCommand.builder()
                .receiverIds(request.getReceiverIds())
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .channel(request.getChannel())
                .build();

        return commandGateway.send(command);
    }
}
