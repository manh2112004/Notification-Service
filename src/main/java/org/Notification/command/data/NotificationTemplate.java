package org.Notification.command.data;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTemplate {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String templateCode;
    // APPLICATION_SUBMITTED, APPLICATION_APPROVED...

    @Column(nullable = false)
    private String titleTemplate;

    @Column(columnDefinition = "TEXT")
    private String contentTemplate;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Boolean isActive = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
