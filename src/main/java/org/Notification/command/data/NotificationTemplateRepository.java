package org.Notification.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, String>, JpaSpecificationExecutor<NotificationTemplate> {
    Optional<NotificationTemplate> findByTemplateCode(String templateCode);
    boolean existsByTemplateCode(String templateCode);
}
