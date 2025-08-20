package com.erp.Service.LedgerService.LedgerServiceNotification;

import com.erp.Model.Ledger;
import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedgerServiceNotificationImpl implements LedgerServiceNotification {

    private final NotificationService notificationService;

    @Autowired
    public LedgerServiceNotificationImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void notifyLedgerCreated(Ledger ledger) {
        send(
                "Ledger Created",
                "A new ledger was created: " + ledger.getName(),
                ledger.getEmail()
        );
    }

    @Override
    public void notifyLedgerUpdated(Ledger ledger) {
        send(
                "Ledger Updated",
                "Ledger details updated for: " + ledger.getName(),
                ledger.getEmail()
        );
    }

    @Override
    public void notifyLedgerDeleted(Ledger ledger) {
        send(
                "Ledger Deleted",
                "Ledger deleted: " + ledger.getName(),
                ledger.getEmail()
        );
    }

    private void send(String title, String message, String to) {
        if (to == null || to.isBlank()) return;

        NotificationMessage notification = new NotificationMessage();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setTimestamp(System.currentTimeMillis());
        notification.setFrom("ERP System");
        notification.setTo(to);

        notificationService.sendNotification(notification);
    }
}
