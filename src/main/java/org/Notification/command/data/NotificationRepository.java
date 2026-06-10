package org.Notification.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>, JpaSpecificationExecutor<Notification> {
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt, n.updatedAt = :updatedAt WHERE n.receiverId = :receiverId AND n.isRead = false")
    void markAllAsRead(
            @Param("receiverId") String receiverId,
            @Param("readAt") LocalDateTime readAt,
            @Param("updatedAt") LocalDateTime updatedAt
    );

    long countByReceiverIdAndIsRead(String receiverId, boolean isRead);
}
