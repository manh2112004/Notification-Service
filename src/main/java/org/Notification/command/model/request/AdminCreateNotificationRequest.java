package org.Notification.command.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Notification.command.data.NotificationChannel;
import org.Notification.command.data.NotificationType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCreateNotificationRequest {
    @NotEmpty(message = "Receiver IDs cannot be empty")
    private List<String> receiverIds;

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotNull(message = "Notification channel is required")
    private NotificationChannel channel;
}
