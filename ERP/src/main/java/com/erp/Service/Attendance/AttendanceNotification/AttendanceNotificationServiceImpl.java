package com.erp.Service.Attendance.AttendanceNotification;

import com.erp.Model.NotificationMessage;
import com.erp.Model.User;
import com.erp.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AttendanceNotificationServiceImpl implements AttendanceNotificationService {

    private final NotificationService notificationService;

    @Autowired
    public AttendanceNotificationServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void sendCheckInNotification(User user, LocalDateTime checkInTime) {
        send("Check-In", "User " + getFullName(user) + " checked in at " + checkInTime, user.getEmail());
    }

    @Override
    public void sendCheckOutNotification(User user, LocalDateTime checkOutTime) {
        send("Check-Out", "User " + getFullName(user) + " checked out at " + checkOutTime, user.getEmail());
    }

    @Override
    public void sendUpdateNotification(User user, LocalDate date) {
        send("Attendance Updated", "Attendance updated for " + getFullName(user) + " on " + date, user.getEmail());
    }

    @Override
    public void sendDeleteNotification(User user, LocalDate date) {
        send("Attendance Deleted", "Attendance deleted for " + getFullName(user) + " on " + date, user.getEmail());
    }

    @Override
    public void sendAutoCheckoutNotification(User user, LocalDateTime checkoutTime) {
        send("Auto Checkout", "Auto checkout completed for " + getFullName(user) + " at " + checkoutTime, user.getEmail());
    }

    private void send(String title, String message, String to) {
        if (to == null || to.isBlank()) return;

        NotificationMessage notification = new NotificationMessage();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setTimestamp(System.currentTimeMillis());
        notification.setFrom("System");
        notification.setTo(to);
        notificationService.sendNotification(notification);
    }

    private String getFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}