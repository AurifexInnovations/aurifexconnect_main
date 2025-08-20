package com.erp.Service.InventoryService.InventoryServiceNotification;

import com.erp.Model.Inventory;

public interface InventoryServiceNotification {

    void notifyInventoryCreated(Inventory inventory);

    void notifyInventoryUpdated(Inventory inventory);

    void notifyInventoryDeleted(Inventory inventory);
}
