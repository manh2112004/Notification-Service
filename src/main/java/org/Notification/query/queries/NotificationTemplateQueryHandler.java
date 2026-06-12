package org.Notification.query.queries;

import org.Notification.command.data.NotificationTemplate;
import org.Notification.command.data.NotificationTemplateRepository;
import org.Notification.query.model.response.NotificationTemplatePageResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class NotificationTemplateQueryHandler {

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @QueryHandler
    @Transactional(readOnly = true)
    public NotificationTemplatePageResponse handle(GetNotificationTemplatesQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());

        Specification<NotificationTemplate> spec = Specification.where(null);

        if (query.getTemplateCode() != null && !query.getTemplateCode().isBlank()) {
            spec = spec.and((root, cq, cb) ->
                    cb.like(cb.lower(root.get("templateCode")), "%" + query.getTemplateCode().toLowerCase() + "%")
            );
        }

        if (query.getType() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("type"), query.getType())
            );
        }

        if (query.getIsActive() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("isActive"), query.getIsActive())
            );
        }

        Page<NotificationTemplate> page = notificationTemplateRepository.findAll(spec, pageable);

        return new NotificationTemplatePageResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public NotificationTemplate handle(GetNotificationTemplateByIdQuery query) {
        return notificationTemplateRepository.findById(query.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy template"));
    }
}
