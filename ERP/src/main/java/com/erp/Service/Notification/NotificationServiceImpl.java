package com.erp.Service.Notification;

import com.erp.Model.NotificationMessage;
import com.erp.Repository.Notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendNotification(NotificationMessage notification) {
        enrichNotification(notification);
        notificationRepository.save(notification);

        // Global broadcast
        messagingTemplate.convertAndSend("/topic/global", notification);

        // Private delivery if recipient exists
        if (hasRecipient(notification)) {
            sendPrivate(notification.getTo(), notification);
        }
    }

    @Override
    public void sendToUser(String username, NotificationMessage notification) {
        notification.setTo(username);
        enrichNotification(notification);
        notificationRepository.save(notification);
        sendPrivate(username, notification);
    }

    @Override
    public void sendToTopic(String topicName, NotificationMessage notification) {
        enrichNotification(notification);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/" + topicName, notification);
    }

    @Override
    public List<NotificationMessage> getNotificationsForUser(String email) {
        return notificationRepository.findByTo(email);
    }

    @Override
    public List<NotificationMessage> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // ---------- Private helpers ----------
    private void enrichNotification(NotificationMessage notification) {
        if (notification.getTimestamp() == 0) {
            notification.setTimestamp(Instant.now().toEpochMilli());
        }
    }

    private boolean hasRecipient(NotificationMessage notification) {
        return notification.getTo() != null && !notification.getTo().isBlank();
    }

    private void sendPrivate(String username, NotificationMessage notification) {
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notification);
    }
}