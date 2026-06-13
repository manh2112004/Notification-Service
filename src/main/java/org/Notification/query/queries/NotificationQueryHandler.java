package org.Notification.query.queries;

import org.Notification.command.data.Notification;
import org.Notification.command.data.NotificationRepository;
import org.Notification.query.model.response.NotificationResponse;
import org.Notification.query.model.response.PageResponse;
import org.springframework.data.jpa.domain.Specification;
import org.Notification.query.queries.GetNotificationByIdQuery;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.Notification.query.queries.GetUnreadNotificationCountQuery;

import org.Notification.command.data.NotificationPreference;
import org.Notification.command.data.NotificationPreferenceRepository;
import org.Notification.query.queries.GetNotificationPreferenceQuery;
import org.Notification.query.model.response.NotificationPreferenceResponse;
import java.time.LocalDateTime;

import org.Notification.query.queries.GetAdminNotificationsQuery;
import org.Notification.command.data.NotificationChannel;
import org.Notification.command.data.NotificationStatus;

import org.Notification.command.data.NotificationDeliveryLog;
import org.Notification.command.data.NotificationDeliveryLogRepository;
import org.Notification.query.model.response.NotificationDeliveryLogResponse;
import org.Notification.query.model.response.NotificationDeliveryLogPageResponse;
import org.Notification.query.queries.GetAdminDeliveryLogByIdQuery;

@Component
public class NotificationQueryHandler {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;

    @Autowired
    private NotificationDeliveryLogRepository notificationDeliveryLogRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public NotificationDeliveryLogPageResponse handle(GetAdminDeliveryLogsQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());
        Specification<NotificationDeliveryLog> spec = Specification.where(null);

        if (query.getNotificationId() != null && !query.getNotificationId().isBlank()) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("notificationId"), query.getNotificationId())
            );
        }

        if (query.getStatus() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("status"), query.getStatus())
            );
        }

        if (query.getChannel() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("channel"), query.getChannel())
            );
        }

        Page<NotificationDeliveryLog> logPage = notificationDeliveryLogRepository.findAll(spec, pageable);

        List<NotificationDeliveryLogResponse> content = logPage.getContent().stream()
                .map(log -> NotificationDeliveryLogResponse.builder()
                        .id(log.getId())
                        .notificationId(log.getNotificationId())
                        .channel(log.getChannel())
                        .status(log.getStatus())
                        .provider(log.getProvider())
                        .errorMessage(log.getErrorMessage())
                        .retryCount(log.getRetryCount())
                        .sentAt(log.getSentAt())
                        .createdAt(log.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return new NotificationDeliveryLogPageResponse(
                content,
                logPage.getNumber(),
                logPage.getSize(),
                logPage.getTotalElements(),
                logPage.getTotalPages()
        );
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public org.Notification.query.model.response.NotificationPageResponse handle(GetAdminNotificationsQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());
        
        Specification<Notification> spec = Specification.where(null);

        if (query.getReceiverId() != null && !query.getReceiverId().isBlank()) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("receiverId"), query.getReceiverId())
            );
        }

        if (query.getIsRead() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("isRead"), query.getIsRead())
            );
        }

        if (query.getType() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("type"), query.getType())
            );
        }

        if (query.getStatus() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("status"), query.getStatus())
            );
        }

        if (query.getChannel() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("channel"), query.getChannel())
            );
        }

        Page<Notification> notificationPage = notificationRepository.findAll(spec, pageable);

        List<NotificationResponse> content = notificationPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new org.Notification.query.model.response.NotificationPageResponse(
                content,
                notificationPage.getNumber(),
                notificationPage.getSize(),
                notificationPage.getTotalElements(),
                notificationPage.getTotalPages()
        );
    }

    @QueryHandler
    @Transactional
    public NotificationPreferenceResponse handle(GetNotificationPreferenceQuery query) {
        NotificationPreference preference = notificationPreferenceRepository.findByUserId(query.getUserId())
                .orElseGet(() -> {
                    NotificationPreference newPref = NotificationPreference.builder()
                            .id(java.util.UUID.randomUUID().toString())
                            .userId(query.getUserId())
                            .emailEnabled(true)
                            .inAppEnabled(true)
                            .smsEnabled(false)
                            .jobAlertEnabled(true)
                            .applicationStatusEnabled(true)
                            .systemEnabled(true)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return notificationPreferenceRepository.save(newPref);
                });

        return NotificationPreferenceResponse.builder()
                .id(preference.getId())
                .userId(preference.getUserId())
                .emailEnabled(preference.getEmailEnabled())
                .inAppEnabled(preference.getInAppEnabled())
                .smsEnabled(preference.getSmsEnabled())
                .jobAlertEnabled(preference.getJobAlertEnabled())
                .applicationStatusEnabled(preference.getApplicationStatusEnabled())
                .systemEnabled(preference.getSystemEnabled())
                .createdAt(preference.getCreatedAt())
                .updatedAt(preference.getUpdatedAt())
                .build();
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public Long handle(GetUnreadNotificationCountQuery query) {
        return notificationRepository.countByReceiverIdAndIsRead(query.getReceiverId(), false);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public NotificationResponse handle(GetNotificationByIdQuery query) {
        Notification notification = notificationRepository.findById(query.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thông báo"));

        if (!notification.getReceiverId().equals(query.getReceiverId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem thông báo này");
        }

        return mapToResponse(notification);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public NotificationResponse handle(GetAdminNotificationByIdQuery query) {
        Notification notification = notificationRepository.findById(query.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thông báo"));

        return mapToResponse(notification);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public NotificationDeliveryLogResponse handle(GetAdminDeliveryLogByIdQuery query) {
        NotificationDeliveryLog log = notificationDeliveryLogRepository.findById(query.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhật ký gửi thông báo"));

        return NotificationDeliveryLogResponse.builder()
                .id(log.getId())
                .notificationId(log.getNotificationId())
                .channel(log.getChannel())
                .status(log.getStatus())
                .provider(log.getProvider())
                .errorMessage(log.getErrorMessage())
                .retryCount(log.getRetryCount())
                .sentAt(log.getSentAt())
                .createdAt(log.getCreatedAt())
                .build();
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public org.Notification.query.model.response.NotificationPageResponse handle(GetNotificationsQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());
        
        Specification<Notification> spec = Specification.where((root, cq, cb) ->
                cb.equal(root.get("receiverId"), query.getReceiverId())
        );

        if (query.getIsRead() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("isRead"), query.getIsRead())
            );
        }

        if (query.getType() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("type"), query.getType())
            );
        }

        Page<Notification> notificationPage = notificationRepository.findAll(spec, pageable);

        List<NotificationResponse> content = notificationPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new org.Notification.query.model.response.NotificationPageResponse(
                content,
                notificationPage.getNumber(),
                notificationPage.getSize(),
                notificationPage.getTotalElements(),
                notificationPage.getTotalPages()
        );
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .receiverId(notification.getReceiverId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .channel(notification.getChannel())
                .status(notification.getStatus())
                .referenceId(notification.getReferenceId())
                .referenceType(notification.getReferenceType())
                .isRead(notification.getIsRead())
                .readAt(notification.getReadAt())
                .sentAt(notification.getSentAt())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}
