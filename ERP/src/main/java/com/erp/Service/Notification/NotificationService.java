package com.erp.Service.Notification;

import com.erp.Model.NotificationMessage;

public interface NotificationService {
    void sendNotification(NotificationMessage message);
    void sendToUser(String username, NotificationMessage message);
    void sendToTopic(String topicName, NotificationMessage message);
}