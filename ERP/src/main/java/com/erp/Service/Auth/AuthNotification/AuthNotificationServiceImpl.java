package com.erp.Service.Auth.AuthNotification;

import com.erp.Model.GenericUser;
import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthNotificationServiceImpl implements AuthNotificationService {

    private final NotificationService notificationService;

    @Autowired
    public AuthNotificationServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void notifyLogin(GenericUser user) {
        send("Login", "User " + user.getEmail() + " logged in successfully.", user.getEmail());
    }

    @Override
    public void notifyLogout(GenericUser user) {
        send("Logout", "User " + user.getEmail() + " logged out successfully.", user.getEmail());
    }

    @Override
    public void notifyRefresh(GenericUser user) {
        send("Session Refreshed", "User " + user.getEmail() + " refreshed their session.", user.getEmail());
    }

    @Override
    public void notifyInactiveLoginAttempt(String email) {
        send("Inactive Login Attempt", "Inactive user attempted to log in: " + email, email);
    }

    @Override
    public void notifyFailedLoginAttempt(String email) {
        send("Failed Login Attempt", "Failed login attempt for email: " + email, email);
    }

    private void send(String title, String message, String to) {
        NotificationMessage notification = new NotificationMessage();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setTimestamp(System.currentTimeMillis());
        notification.setFrom("System");
        notification.setTo(to);
        notificationService.sendNotification(notification);
    }
}