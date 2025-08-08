package com.erp.Controller.Notification;

import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public String send(@RequestBody NotificationMessage message) {
        notificationService.sendNotification(message);
        return "Notification sent";
    }
}