package com.erp.Service.LineItems.LineItemsNotification;

import com.erp.Model.LineItems;

public interface LineItemServiceNotification {
    void notifyLineItemCreated(LineItems lineItem);
    void notifyLineItemUpdated(LineItems lineItem); // Optional for future use
    void notifyLineItemDeleted(LineItems lineItem); // Optional for future use
}
