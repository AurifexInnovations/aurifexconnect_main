package com.erp.Service.Notification;

import com.erp.Model.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(NotificationMessage message) {

        System.out.println(" Sending notification: " + message);

        // Send notification to all subscribers of /topic/global
        messagingTemplate.convertAndSend("/topic/global", message);
    }
}
