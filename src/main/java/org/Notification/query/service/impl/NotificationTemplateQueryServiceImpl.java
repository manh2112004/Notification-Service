package org.Notification.query.service.impl;

import org.Notification.command.data.NotificationTemplate;
import org.Notification.command.data.NotificationType;
import org.Notification.query.model.response.NotificationTemplatePageResponse;
import org.Notification.query.queries.GetNotificationTemplateByIdQuery;
import org.Notification.query.queries.GetNotificationTemplatesQuery;
import org.Notification.query.service.NotificationTemplateQueryService;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NotificationTemplateQueryServiceImpl implements NotificationTemplateQueryService {

    @Autowired
    private QueryGateway queryGateway;

    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<NotificationTemplatePageResponse> getTemplates(
            int page, int size, String templateCode, NotificationType type, Boolean isActive
    ) {
        GetNotificationTemplatesQuery query = GetNotificationTemplatesQuery.builder()
                .page(page)
                .size(size)
                .templateCode(templateCode)
                .type(type)
                .isActive(isActive)
                .build();

        return queryGateway.query(
                query,
                ResponseTypes.instanceOf(NotificationTemplatePageResponse.class)
        ).thenApply(response -> (NotificationTemplatePageResponse) response);
    }

    @Override
    public CompletableFuture<NotificationTemplate> getTemplateById(String id) {
        GetNotificationTemplateByIdQuery query = GetNotificationTemplateByIdQuery.builder()
                .id(id)
                .build();

        return queryGateway.query(
                query,
                ResponseTypes.instanceOf(NotificationTemplate.class)
        );
    }
}
