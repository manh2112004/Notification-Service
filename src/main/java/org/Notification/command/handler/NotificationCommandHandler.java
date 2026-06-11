package org.Notification.command.handler;

import org.Notification.client.UserClient;
import org.Notification.command.command.CreateAdminNotificationCommand;
import org.Notification.command.command.BroadcastAdminNotificationCommand;
import org.Notification.command.data.Notification;
import org.Notification.command.data.NotificationRepository;
import org.Notification.command.data.NotificationStatus;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NotificationCommandHandler {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserClient userClient;

    @CommandHandler
    public List<Notification> handle(CreateAdminNotificationCommand command) {
        // Validate all receiverIds exist
        for (String receiverId : command.getReceiverIds()) {
            boolean userExists = userClient.checkUserExists(receiverId);
            if (!userExists) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Người nhận thông báo " + receiverId + " không tồn tại trong hệ thống"
                );
            }
        }

        List<Notification> createdNotifications = new ArrayList<>();

        for (String receiverId : command.getReceiverIds()) {
            Notification notification = Notification.builder()
                    .id(UUID.randomUUID().toString())
                    .receiverId(receiverId)
                    .title(command.getTitle())
                    .content(command.getContent())
                    .type(command.getType())
                    .channel(command.getChannel())
                    .status(NotificationStatus.SENT)
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .sentAt(LocalDateTime.now())
                    .build();

            createdNotifications.add(notificationRepository.save(notification));
        }

        return createdNotifications;
    }

    @CommandHandler
    public List<Notification> handle(BroadcastAdminNotificationCommand command) {
        List<String> allUserIds = userClient.getAllUserIds(command.getToken());
        List<Notification> createdNotifications = new ArrayList<>();

        for (String receiverId : allUserIds) {
            Notification notification = Notification.builder()
                    .id(UUID.randomUUID().toString())
                    .receiverId(receiverId)
                    .title(command.getTitle())
                    .content(command.getContent())
                    .type(command.getType())
                    .channel(command.getChannel())
                    .status(NotificationStatus.SENT)
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .sentAt(LocalDateTime.now())
                    .build();

            createdNotifications.add(notificationRepository.save(notification));
        }

        return createdNotifications;
    }
}
