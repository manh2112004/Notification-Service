package org.Notification.command.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateNotificationPreferenceRequest {
    private Boolean emailEnabled;
    private Boolean inAppEnabled;
    private Boolean smsEnabled;
    private Boolean jobAlertEnabled;
    private Boolean applicationStatusEnabled;
    private Boolean systemEnabled;
}
