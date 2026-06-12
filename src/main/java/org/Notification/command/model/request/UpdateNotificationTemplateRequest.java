package org.Notification.command.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Notification.command.data.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateNotificationTemplateRequest {
    @NotBlank(message = "Template code is required")
    private String templateCode;

    @NotBlank(message = "Title template is required")
    private String titleTemplate;

    @NotBlank(message = "Content template is required")
    private String contentTemplate;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotNull(message = "Active status is required")
    private Boolean isActive;
}
