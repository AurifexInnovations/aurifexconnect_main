package com.erp.Controller.Notification;

import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Send a notification to everyone subscribed to /topic/global
     */
    @PostMapping("/send/global")
    public ResponseEntity<String> sendGlobal(@Valid @RequestBody NotificationMessage message) {
        notificationService.sendNotification(message); // Your service already sends to /topic/global
        return ResponseEntity.ok("Global notification sent");
    }

    /**
     * Send a notification to a specific user (private queue)
     */
    @PostMapping("/send/user/{username}")
    public ResponseEntity<String> sendToUser(
            @PathVariable String username,
            @Valid @RequestBody NotificationMessage message) {
        notificationService.sendToUser(username, message); // Implemented in your service
        return ResponseEntity.ok("Notification sent to user: " + username);
    }

    /**
     * Send a notification to a custom topic (e.g., /topic/sales, /topic/hr)
     */
    @PostMapping("/send/topic/{topicName}")
    public ResponseEntity<String> sendToTopic(
            @PathVariable String topicName,
            @Valid @RequestBody NotificationMessage message) {
        notificationService.sendToTopic(topicName, message); // Implemented in your service
        return ResponseEntity.ok("Notification sent to topic: " + topicName);
    }
}
