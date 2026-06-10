package org.Notification.command.controller;

import org.Notification.command.data.NotificationPreference;
import org.Notification.command.data.NotificationPreferenceRepository;
import org.Notification.command.model.request.UpdateNotificationPreferenceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/notification-preferences")
public class NotificationPreferenceCommandController {

    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public NotificationPreference updatePreference(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateNotificationPreferenceRequest request
    ) {
        NotificationPreference preference = notificationPreferenceRepository.findByUserId(jwt.getSubject())
                .orElseGet(() -> NotificationPreference.builder()
                        .id(java.util.UUID.randomUUID().toString())
                        .userId(jwt.getSubject())
                        .createdAt(LocalDateTime.now())
                        .build());

        if (request.getEmailEnabled() != null) {
            preference.setEmailEnabled(request.getEmailEnabled());
        }
        if (request.getInAppEnabled() != null) {
            preference.setInAppEnabled(request.getInAppEnabled());
        }
        if (request.getSmsEnabled() != null) {
            preference.setSmsEnabled(request.getSmsEnabled());
        }
        if (request.getJobAlertEnabled() != null) {
            preference.setJobAlertEnabled(request.getJobAlertEnabled());
        }
        if (request.getApplicationStatusEnabled() != null) {
            preference.setApplicationStatusEnabled(request.getApplicationStatusEnabled());
        }
        if (request.getSystemEnabled() != null) {
            preference.setSystemEnabled(request.getSystemEnabled());
        }

        preference.setUpdatedAt(LocalDateTime.now());
        
        return notificationPreferenceRepository.save(preference);
    }
}
