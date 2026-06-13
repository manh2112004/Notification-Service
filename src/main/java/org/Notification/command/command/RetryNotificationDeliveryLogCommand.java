package org.Notification.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetryNotificationDeliveryLogCommand {
    private String id;
    private String token;
}
