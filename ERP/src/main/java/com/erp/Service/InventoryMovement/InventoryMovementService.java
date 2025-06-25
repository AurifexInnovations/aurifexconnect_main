package com.erp.Service.InventoryMovement;

import com.erp.Dto.Request.InventoryMovementRequest;
import com.erp.Dto.Request.InventoryMovementSummaryRequest;
import com.erp.Dto.Response.InventoryMovementResponse;
import com.erp.Dto.Response.InventoryMovementSummaryResponse;

import java.util.List;


public interface InventoryMovementService {

    InventoryMovementResponse processInventoryMovement(InventoryMovementRequest inventoryMovementRequest);

    List<InventoryMovementSummaryResponse> getInventoryMovementSummary(InventoryMovementSummaryRequest request);
}
