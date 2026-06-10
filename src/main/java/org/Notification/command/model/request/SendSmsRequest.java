package org.Notification.command.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Notification.command.data.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendSmsRequest {
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Message is required")
    private String message;

    private NotificationType type;

    private String receiverId;
}
