package org.Notification.command.handler;

import org.Notification.command.command.CreateNotificationTemplateCommand;
import org.Notification.command.data.NotificationTemplate;
import org.Notification.command.data.NotificationTemplateRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class NotificationTemplateCommandHandler {

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @CommandHandler
    public NotificationTemplate handle(CreateNotificationTemplateCommand command) {
        if (notificationTemplateRepository.existsByTemplateCode(command.getTemplateCode())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Template code '" + command.getTemplateCode() + "' đã tồn tại"
            );
        }

        NotificationTemplate template = NotificationTemplate.builder()
                .id(UUID.randomUUID().toString())
                .templateCode(command.getTemplateCode())
                .titleTemplate(command.getTitleTemplate())
                .contentTemplate(command.getContentTemplate())
                .type(command.getType())
                .isActive(command.getIsActive() != null ? command.getIsActive() : true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return notificationTemplateRepository.save(template);
    }
}
