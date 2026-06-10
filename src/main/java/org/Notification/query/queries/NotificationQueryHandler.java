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

@Component
public class NotificationQueryHandler {

    @Autowired
    private NotificationRepository notificationRepository;

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
