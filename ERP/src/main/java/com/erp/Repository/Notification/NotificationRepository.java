package com.erp.Repository.Notification;

import com.erp.Model.NotificationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationMessage, Long> {
    List<NotificationMessage> findByToAndReadFalse(String to);
    List<NotificationMessage> findByTo(String to);
}
