package com.erp.Service.LineItems.LineItemsNotification;

import com.erp.Model.LineItems;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LineItemServiceNotificationImpl implements LineItemServiceNotification {

    @Override
    public void notifyLineItemCreated(LineItems lineItem) {
        log.info("✅ Line Item Created: ID={}, Name={}, Qty={}",
                lineItem.getLineItemId(),
                lineItem.getItemName(),
                lineItem.getQuantity());
        // Future: send email, push, websocket etc.
    }

    @Override
    public void notifyLineItemUpdated(LineItems lineItem) {
        log.info("✏️ Line Item Updated: ID={}, Name={}, Qty={}",
                lineItem.getLineItemId(),
                lineItem.getItemName(),
                lineItem.getQuantity());
    }

    @Override
    public void notifyLineItemDeleted(LineItems lineItem) {
        log.info("❌ Line Item Deleted: ID={}, Name={}, Qty={}",
                lineItem.getLineItemId(),
                lineItem.getItemName(),
                lineItem.getQuantity());
    }
}
