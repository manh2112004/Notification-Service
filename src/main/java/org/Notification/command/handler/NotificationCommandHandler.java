package org.Notification.command.handler;

import org.Notification.client.UserClient;
import org.Notification.command.command.CreateAdminNotificationCommand;
import org.Notification.command.command.BroadcastAdminNotificationCommand;
import org.Notification.command.command.RetryNotificationDeliveryLogCommand;
import org.Notification.command.data.*;
import org.Notification.command.service.MailService;
import org.Notification.command.service.SmsService;
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
    private NotificationDeliveryLogRepository notificationDeliveryLogRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private MailService mailService;

    @Autowired
    private SmsService smsService;

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

    @CommandHandler
    public NotificationDeliveryLog handle(RetryNotificationDeliveryLogCommand command) {
        NotificationDeliveryLog log = notificationDeliveryLogRepository.findById(command.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy log gửi thông báo"));

        Notification notification = notificationRepository.findById(log.getNotificationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thông báo tương ứng"));

        log.setRetryCount(log.getRetryCount() + 1);
        try {
            if (log.getChannel() == NotificationChannel.EMAIL) {
                String email = userClient.getUserEmail(notification.getReceiverId(), command.getToken());
                if (email == null || email.isBlank()) {
                    throw new IllegalArgumentException("Không tìm thấy email của người nhận");
                }
                mailService.sendHtmlEmail(email, notification.getTitle(), notification.getContent());
            } else if (log.getChannel() == NotificationChannel.SMS) {
                String phoneNumber = userClient.getUserPhoneNumber(notification.getReceiverId());
                if (phoneNumber == null || phoneNumber.isBlank()) {
                    throw new IllegalArgumentException("Không tìm thấy số điện thoại của người nhận");
                }
                smsService.sendSms(phoneNumber, notification.getContent());
            } else if (log.getChannel() == NotificationChannel.IN_APP || log.getChannel() == NotificationChannel.WEBSOCKET) {
                // Đối với kênh IN_APP và WEBSOCKET, chỉ cần đánh dấu thành công vì không qua nhà cung cấp ngoài
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kênh gửi thông báo không hỗ trợ retry: " + log.getChannel());
            }

            log.setStatus(DeliveryStatus.SUCCESS);
            log.setErrorMessage(null);
            log.setSentAt(LocalDateTime.now());

            notification.setStatus(NotificationStatus.SENT);
            notificationRepository.save(notification);

        } catch (Exception e) {
            log.setStatus(DeliveryStatus.FAILED);
            log.setErrorMessage(e.getMessage());
            notificationDeliveryLogRepository.save(log);
            if (e instanceof ResponseStatusException) {
                throw (ResponseStatusException) e;
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gửi lại thông báo thất bại: " + e.getMessage());
        }

        return notificationDeliveryLogRepository.save(log);
    }
}
