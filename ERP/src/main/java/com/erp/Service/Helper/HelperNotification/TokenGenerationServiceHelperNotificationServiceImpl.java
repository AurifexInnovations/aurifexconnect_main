package com.erp.Service.Helper.HelperNotification;

import com.erp.Model.NotificationMessage;
import com.erp.Security.JWT.TokenType;
import com.erp.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class TokenGenerationServiceHelperNotificationServiceImpl implements TokenGenerationServiceHelperNotification {

    private final NotificationService notificationService;

    @Autowired
    public TokenGenerationServiceHelperNotificationServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void notifyTokenGenerated(TokenType tokenType, Map<String, Object> claims, Instant expiry) {
        String userEmail = (String) claims.getOrDefault("email", "Unknown");
        String message = String.format(
                "A %s token was generated for %s. Expiry: %s",
                tokenType.name(),
                userEmail,
                expiry
        );

        NotificationMessage notification = new NotificationMessage();
        notification.setTitle("Token Generated");
        notification.setMessage(message);
        notification.setTimestamp(System.currentTimeMillis());
        notification.setFrom("System");
        notification.setTo(userEmail);

        notificationService.sendNotification(notification);
    }
}
