package org.Notification.event;

import org.Notification.command.data.*;
import org.Notification.command.service.MailService;
import org.Notification.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class KafkaEventConsumer {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationDeliveryLogRepository notificationDeliveryLogRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserClient userClient;

    @KafkaListener(topics = {
        KafkaTopic.USER_EVENTS,
        KafkaTopic.PROFILE_EVENTS,
        KafkaTopic.COMPANY_EVENTS,
        KafkaTopic.JOB_EVENTS,
        KafkaTopic.APPLICATION_EVENTS
    }, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeEvent(KafkaEvent event) {
        log.info("Consumed Kafka event: {}", event);
        try {
            // Determine type
            NotificationType type = NotificationType.SYSTEM;
            String eventType = event.getEventType();
            if (eventType == null) {
                eventType = "UnknownEvent";
            }
            
            if (eventType.startsWith("User") || eventType.startsWith("Password") || eventType.startsWith("Account")) {
                type = NotificationType.ACCOUNT;
            } else if (eventType.startsWith("Profile")) {
                type = NotificationType.RESUME_UPDATE;
            } else if (eventType.startsWith("Company")) {
                type = NotificationType.COMPANY_UPDATE;
            } else if (eventType.startsWith("Job")) {
                type = NotificationType.JOB_ALERT;
            } else if (eventType.startsWith("Application")) {
                type = NotificationType.APPLICATION_STATUS;
            }

            // Create IN_APP notification
            Notification inAppNotification = Notification.builder()
                    .id(UUID.randomUUID().toString())
                    .receiverId(event.getUserId() != null ? event.getUserId() : "SYSTEM")
                    .title(event.getTitle())
                    .content(event.getMessage())
                    .type(type)
                    .channel(NotificationChannel.IN_APP)
                    .status(NotificationStatus.SENT)
                    .referenceId(event.getReferenceId())
                    .referenceType(event.getReferenceType())
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .sentAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(inAppNotification);

            // Create DeliveryLog for IN_APP
            NotificationDeliveryLog inAppLog = NotificationDeliveryLog.builder()
                    .id(UUID.randomUUID().toString())
                    .notificationId(inAppNotification.getId())
                    .channel(NotificationChannel.IN_APP)
                    .status(DeliveryStatus.SUCCESS)
                    .provider("IN_APP")
                    .retryCount(0)
                    .createdAt(LocalDateTime.now())
                    .sentAt(LocalDateTime.now())
                    .build();
            notificationDeliveryLogRepository.save(inAppLog);

            // Determine if we should also send EMAIL
            boolean shouldSendEmail = eventType.equals("UserRegisteredEvent")
                    || eventType.equals("PasswordChangedEvent")
                    || eventType.equals("CompanyApprovedEvent")
                    || eventType.equals("CompanyRejectedEvent")
                    || eventType.equals("ApplicationAcceptedEvent")
                    || eventType.equals("ApplicationRejectedEvent");

            if (shouldSendEmail && event.getUserId() != null) {
                // Create EMAIL notification
                Notification emailNotification = Notification.builder()
                        .id(UUID.randomUUID().toString())
                        .receiverId(event.getUserId())
                        .title(event.getTitle())
                        .content(event.getMessage())
                        .type(type)
                        .channel(NotificationChannel.EMAIL)
                        .status(NotificationStatus.SENT)
                        .referenceId(event.getReferenceId())
                        .referenceType(event.getReferenceType())
                        .isRead(false)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .sentAt(LocalDateTime.now())
                        .build();

                notificationRepository.save(emailNotification);

                // Try sending email
                DeliveryStatus emailStatus = DeliveryStatus.SUCCESS;
                String errorMsg = null;
                try {
                    String recipientEmail = null;
                    if (event.getUserId().contains("@")) {
                        recipientEmail = event.getUserId();
                    } else {
                        recipientEmail = "user-" + event.getUserId() + "@example.com";
                    }

                    mailService.sendHtmlEmail(recipientEmail, event.getTitle(), event.getMessage());
                } catch (Exception ex) {
                    log.error("Failed to send HTML email: ", ex);
                    emailStatus = DeliveryStatus.FAILED;
                    errorMsg = ex.getMessage();
                }

                // Create DeliveryLog for EMAIL
                NotificationDeliveryLog emailLog = NotificationDeliveryLog.builder()
                        .id(UUID.randomUUID().toString())
                        .notificationId(emailNotification.getId())
                        .channel(NotificationChannel.EMAIL)
                        .status(emailStatus)
                        .provider("EMAIL")
                        .errorMessage(errorMsg)
                        .retryCount(0)
                        .createdAt(LocalDateTime.now())
                        .sentAt(LocalDateTime.now())
                        .build();
                notificationDeliveryLogRepository.save(emailLog);
            }

        } catch (Exception e) {
            log.error("Error processing consumed Kafka event: ", e);
        }
    }
}
