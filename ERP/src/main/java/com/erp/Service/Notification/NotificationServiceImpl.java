package com.erp.Service.Notification;

import com.erp.Model.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendNotification(NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/global", message);
    }

    @Override
    public void sendToUser(String username, NotificationMessage message) {
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
    }

    @Override
    public void sendToTopic(String topicName, NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/" + topicName, message);
    }

}
