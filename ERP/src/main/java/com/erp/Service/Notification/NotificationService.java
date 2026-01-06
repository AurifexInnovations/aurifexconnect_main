package com.erp.Service.Notification;

import com.erp.Model.Task;

public interface NotificationService {
    void sendTaskStartedNotification(Task task, Long technicianId);
}
