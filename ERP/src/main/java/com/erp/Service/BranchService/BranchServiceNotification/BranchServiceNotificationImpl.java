package com.erp.Service.BranchService.BranchServiceNotification;

import com.erp.Model.Branch;
import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchServiceNotificationImpl implements BranchServiceNotification {

    private final NotificationService notificationService;

    @Autowired
    public BranchServiceNotificationImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void notifyBranchCreated(Branch branch) {
        send("Branch Created",
                "New branch created: " + getBranchLabel(branch),
                "admin@erp.com");
    }

    @Override
    public void notifyBranchUpdated(Branch branch) {
        send("Branch Updated",
                "Branch updated: " + getBranchLabel(branch),
                "admin@erp.com");
    }

    @Override
    public void notifyBranchDeleted(Branch branch) {
        send("Branch Deleted",
                "Branch deleted: " + getBranchLabel(branch),
                "admin@erp.com");
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

    private String getBranchLabel(Branch branch) {
        return branch.getBranchName() + " - " + branch.getLocation();
    }
}
