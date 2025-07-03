package com.erp.Service.InventoryMovement;

import com.erp.Dto.Request.InventoryMovementRequest;
import com.erp.Dto.Response.InventoryMovementResponse;
import org.springframework.web.bind.annotation.PathVariable;


public interface InventoryMovementService {

    InventoryMovementResponse processInventoryMovement(InventoryMovementRequest inventoryMovementRequest);

}
