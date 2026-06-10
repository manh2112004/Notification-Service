package org.Notification.command.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Notification.command.data.NotificationChannel;
import org.Notification.command.data.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationRequest {
    @NotBlank(message = "Receiver ID is required")
    private String receiverId;

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotNull(message = "Notification channel is required")
    private NotificationChannel channel;

    private String referenceId;
    private String referenceType;
}
