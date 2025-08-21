package com.erp.Controller.Notification;

import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
        notificationService.sendNotification(message);
        return ResponseEntity.ok("Global notification sent");
    }

    /**
     * Send a notification to a specific user (private queue)
     */
    @PostMapping("/send/user/{username}")
    public ResponseEntity<String> sendToUser(
            @PathVariable String username,
            @Valid @RequestBody NotificationMessage message) {
        notificationService.sendToUser(username, message);
        return ResponseEntity.ok("Notification sent to user: " + username);
    }

    /**
     * Send a notification to a custom topic (e.g., /topic/sales, /topic/hr)
     */
    @PostMapping("/send/topic/{topicName}")
    public ResponseEntity<String> sendToTopic(
            @PathVariable String topicName,
            @Valid @RequestBody NotificationMessage message) {
        notificationService.sendToTopic(topicName, message);
        return ResponseEntity.ok("Notification sent to topic: " + topicName);
    }

    /**
     * Get all notifications for a specific user
     */
    @GetMapping("/user/{email}")
    public ResponseEntity<List<NotificationMessage>> getUserNotifications(@PathVariable String email) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(email));
    }

    /**
     * Get all notifications
     */
    @GetMapping
    public ResponseEntity<List<NotificationMessage>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
}