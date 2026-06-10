package org.Notification.command.controller;

import jakarta.validation.Valid;
import org.Notification.command.data.Notification;
import org.Notification.command.data.NotificationRepository;
import org.Notification.command.data.NotificationStatus;
import org.Notification.command.model.request.CreateNotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/internal/notifications")
public class NotificationInternalCommandController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private org.Notification.client.UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification createNotification(@Valid @RequestBody CreateNotificationRequest request) {
        // Kiểm tra xem receiverId có tồn tại trong hệ thống hay không (thông qua Profile-Service)
        boolean userExists = userClient.checkUserExists(request.getReceiverId());
        if (!userExists) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Người nhận thông báo không tồn tại trong hệ thống"
            );
        }

        Notification notification = Notification.builder()
                .id(UUID.randomUUID().toString())
                .receiverId(request.getReceiverId())
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .channel(request.getChannel())
                .status(NotificationStatus.SENT)
                .referenceId(request.getReferenceId())
                .referenceType(request.getReferenceType())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .sentAt(LocalDateTime.now())
                .build();

        return notificationRepository.save(notification);
    }
}
