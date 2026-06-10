package org.Notification.command.data;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference {

    @Id
    private String id;

    @Column(nullable = false)
    private String userId;

    private Boolean emailEnabled = true;

    private Boolean inAppEnabled = true;

    private Boolean smsEnabled = false;

    private Boolean jobAlertEnabled = true;

    private Boolean applicationStatusEnabled = true;

    private Boolean systemEnabled = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}