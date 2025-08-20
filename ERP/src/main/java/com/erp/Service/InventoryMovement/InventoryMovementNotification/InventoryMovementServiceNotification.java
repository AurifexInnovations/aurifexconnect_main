package com.erp.Service.InventoryMovement.InventoryMovementNotification;

import com.erp.Model.InventoryMovement;

public interface InventoryMovementServiceNotification {
    void notifyInventoryMoved(InventoryMovement inventoryMovement);
}
