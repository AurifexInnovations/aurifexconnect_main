package com.erp.Controller.WebSocketNotification;

import com.erp.Model.NotificationMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;

@Controller
public class WebSocketMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketMessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Clients send messages to: /app/chat
     * Then, server broadcasts to: /topic/chat
     */
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public NotificationMessage handleChat(NotificationMessage message) {
        // Add timestamp & system defaults
        message.setTimestamp(Instant.now().toEpochMilli());
        if (message.getFrom() == null) {
            message.setFrom("Anonymous");
        }
        return message;
    }

    /**
     * Clients send private messages to: /app/private-message
     * Server delivers them to: /user/{recipient}/queue/private
     */
    @MessageMapping("/private-message")
    public void sendPrivateMessage(NotificationMessage message, Principal principal) {
        // Add timestamp if missing
        if (message.getTimestamp() == 0) {
            message.setTimestamp(Instant.now().toEpochMilli());
        }

        // If sender is not provided, use Principal name
        if (message.getFrom() == null && principal != null) {
            message.setFrom(principal.getName());
        }

        // Deliver to recipient's personal queue
        messagingTemplate.convertAndSendToUser(
                message.getTo(),       // recipient username/email
                "/queue/private",      // destination
                message
        );
    }
}