package com.erp.Service.BankAccount.BankAccountNotification;

import com.erp.Model.BankAccount;
import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountNotificationServiceImpl implements BankAccountNotification {

    private final NotificationService notificationService;

    @Autowired
    public BankAccountNotificationServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void notifyBankAccountCreated(BankAccount bankAccount) {
        send("Bank Account Created",
                "New bank account created: " + getAccountLabel(bankAccount),
                "admin@erp.com");
    }

    @Override
    public void notifyBankAccountUpdated(BankAccount bankAccount) {
        send("Bank Account Updated",
                "Bank account updated: " + getAccountLabel(bankAccount),
                "admin@erp.com");
    }

    @Override
    public void notifyBankAccountDeleted(BankAccount bankAccount) {
        send("Bank Account Deleted",
                "Bank account deleted: " + getAccountLabel(bankAccount),
                "admin@erp.com");
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

    private String getAccountLabel(BankAccount bankAccount) {
        return bankAccount.getBankName() + " (" + bankAccount.getAccountNumber() + ")";
    }
}