package org.Notification.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreferenceResponse {
    private String id;
    private String userId;
    private Boolean emailEnabled;
    private Boolean inAppEnabled;
    private Boolean smsEnabled;
    private Boolean jobAlertEnabled;
    private Boolean applicationStatusEnabled;
    private Boolean systemEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
