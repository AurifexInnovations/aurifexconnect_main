package com.erp.Service.LeaveService.LeaveServiceNotification;

import com.erp.Model.Leave;
import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveServiceNotificationImpl implements LeaveServiceNotification {

    private final NotificationService notificationService;

    @Autowired
    public LeaveServiceNotificationImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void notifyLeaveCreated(Leave leave) {
        send("Leave Request Created",
                "Leave request created by " + leave.getUser().getEmail() +
                        " from " + leave.getStartDate() + " to " + leave.getEndDate(),
                leave.getUser().getEmail());
    }

    @Override
    public void notifyLeaveUpdated(Leave leave) {
        send("Leave Request Updated",
                "Leave request updated for " + leave.getUser().getEmail() +
                        " from " + leave.getStartDate() + " to " + leave.getEndDate(),
                leave.getUser().getEmail());
    }

    @Override
    public void notifyLeaveDeleted(Leave leave) {
        send("Leave Request Deleted",
                "Leave request deleted for " + leave.getUser().getEmail() +
                        " for dates " + leave.getStartDate() + " to " + leave.getEndDate(),
                leave.getUser().getEmail());
    }

    @Override
    public void notifyLeaveStatusChanged(Leave leave, String oldStatus, String newStatus) {
        send("Leave Status Changed",
                "Leave request status changed for " + leave.getUser().getEmail() +
                        " from " + oldStatus + " to " + newStatus,
                leave.getUser().getEmail());
    }

    private void send(String title, String message, String to) {
        NotificationMessage notification = new NotificationMessage(
                title,
                message,
                System.currentTimeMillis(),
                "System",
                to
        );
        notificationService.sendNotification(notification);
    }
}
