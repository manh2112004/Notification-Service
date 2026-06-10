package org.Notification.command.data;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_delivery_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDeliveryLog {

    @Id
    private String id;

    @Column(nullable = false)
    private String notificationId;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private String provider;
    // EMAIL, FIREBASE, WEBSOCKET...

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private Integer retryCount = 0;

    private LocalDateTime sentAt;

    private LocalDateTime createdAt;
}