package org.Notification.query.service.impl;

import org.Notification.command.data.DeliveryStatus;
import org.Notification.command.data.NotificationChannel;
import org.Notification.query.model.response.NotificationDeliveryLogPageResponse;
import org.Notification.query.queries.GetAdminDeliveryLogsQuery;
import org.Notification.query.service.NotificationDeliveryLogQueryService;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NotificationDeliveryLogQueryServiceImpl implements NotificationDeliveryLogQueryService {

    @Autowired
    private QueryGateway queryGateway;

    @Override
    public CompletableFuture<NotificationDeliveryLogPageResponse> getDeliveryLogs(
            int page, int size, String notificationId, DeliveryStatus status, NotificationChannel channel
    ) {
        GetAdminDeliveryLogsQuery query = GetAdminDeliveryLogsQuery.builder()
                .page(page)
                .size(size)
                .notificationId(notificationId)
                .status(status)
                .channel(channel)
                .build();

        return queryGateway.query(
                query,
                ResponseTypes.instanceOf(NotificationDeliveryLogPageResponse.class)
        );
    }
}
