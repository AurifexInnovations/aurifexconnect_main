package com.erp.Service.Attendance.AttendanceNotification;

import com.erp.Model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AttendanceNotificationService {
    void sendCheckInNotification(User user, LocalDateTime checkInTime);
    void sendCheckOutNotification(User user, LocalDateTime checkOutTime);
    void sendUpdateNotification(User user, LocalDate date);
    void sendDeleteNotification(User user, LocalDate date);
    void sendAutoCheckoutNotification(User user, LocalDateTime checkoutTime);
}
