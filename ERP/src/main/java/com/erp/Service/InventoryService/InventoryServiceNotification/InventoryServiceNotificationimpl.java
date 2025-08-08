package com.erp.Service.InventoryService.InventoryServiceNotification;

import com.erp.Model.Inventory;
import com.erp.Model.NotificationMessage;
import com.erp.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceNotificationimpl implements InventoryServiceNotification {

    private final NotificationService notificationService;

    @Autowired
    public InventoryServiceNotificationimpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void notifyInventoryCreated(Inventory inventory) {
        send("Inventory Created",
                "New item added: " + getLabel(inventory),
                "admin@erp.com");
    }

    @Override
    public void notifyInventoryUpdated(Inventory inventory) {
        send("Inventory Updated",
                "Inventory item updated: " + getLabel(inventory),
                "admin@erp.com");
    }

    @Override
    public void notifyInventoryDeleted(Inventory inventory) {
        send("Inventory Deleted",
                "Inventory item deleted: " + getLabel(inventory),
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

    private String getLabel(Inventory inventory) {
        return inventory.getItemName() + " (Qty: " + inventory.getItemQuantity() + ")";
    }
}
