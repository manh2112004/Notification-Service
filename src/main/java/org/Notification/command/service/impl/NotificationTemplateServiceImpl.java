package org.Notification.command.service.impl;

import org.Notification.command.command.CreateNotificationTemplateCommand;
import org.Notification.command.data.NotificationTemplate;
import org.Notification.command.model.request.CreateNotificationTemplateRequest;
import org.Notification.command.service.NotificationTemplateService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    @Autowired
    private CommandGateway commandGateway;

    @Override
    public CompletableFuture<NotificationTemplate> createTemplate(CreateNotificationTemplateRequest request) {
        CreateNotificationTemplateCommand command = CreateNotificationTemplateCommand.builder()
                .templateCode(request.getTemplateCode())
                .titleTemplate(request.getTitleTemplate())
                .contentTemplate(request.getContentTemplate())
                .type(request.getType())
                .isActive(request.getIsActive())
                .build();

        return commandGateway.send(command);
    }
}
