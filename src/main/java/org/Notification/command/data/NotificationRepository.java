package org.Notification.command.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    
    @Query("SELECT n FROM Notification n WHERE n.receiverId = :receiverId " +
           "AND (:isRead IS NULL OR n.isRead = :isRead) " +
           "AND (:type IS NULL OR n.type = :type)")
    Page<Notification> findNotifications(
        @Param("receiverId") String receiverId,
        @Param("isRead") Boolean isRead,
        @Param("type") NotificationType type,
        Pageable pageable
    );
}
