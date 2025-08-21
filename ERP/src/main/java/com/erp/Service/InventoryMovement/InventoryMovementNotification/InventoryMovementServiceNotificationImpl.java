package com.erp.Service.InventoryMovement.InventoryMovementNotification;

import com.erp.Model.InventoryMovement;
import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryMovementServiceNotificationImpl implements InventoryMovementServiceNotification {

    private final NotificationService notificationService;

    @Autowired
    public InventoryMovementServiceNotificationImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void notifyInventoryMoved(InventoryMovement inventoryMovement) {
        String itemName = inventoryMovement.getInventory().getItemName();
        String branchName = inventoryMovement.getBranch().getBranchName();
        double qty = inventoryMovement.getInventory().getItemQuantity();

        String message = String.format(
                "Inventory moved: %s (%.2f units) at branch %s",
                itemName,
                qty,
                branchName
        );

        NotificationMessage notification = new NotificationMessage();
        notification.setTitle("Inventory Movement");
        notification.setMessage(message);
        notification.setTimestamp(System.currentTimeMillis());
        notification.setFrom("System");
        notification.setTo("admin@erp.com");

        notificationService.sendNotification(notification);
    }
}