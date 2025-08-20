package com.erp.Service.Notification;

import com.erp.Model.NotificationMessage;
import java.util.List;

public interface NotificationService {

    void sendNotification(NotificationMessage notification);

    void sendToUser(String username, NotificationMessage notification);

    void sendToTopic(String topicName, NotificationMessage notification);

    List<NotificationMessage> getNotificationsForUser(String email);

    List<NotificationMessage> getAllNotifications();
}
