package org.Notification.command.model.request;

import jakarta.validation.constraints.Email;
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
public class SendEmailRequest {
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String recipientEmail;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Body is required")
    private String body;

    private NotificationType type;

    private String receiverId;
}
