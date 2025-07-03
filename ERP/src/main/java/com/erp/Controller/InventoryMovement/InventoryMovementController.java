package com.erp.Controller.InventoryMovement;

import com.erp.Dto.Request.InventoryMovementRequest;
import com.erp.Dto.Request.InventoryMovementSummaryRequest;
import com.erp.Dto.Response.InventoryMovementResponse;
import com.erp.Dto.Response.InventoryMovementSummaryResponse;
import com.erp.Service.InventoryMovement.InventoryMovementService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/inventory-movement")
public class InventoryMovementController {

    private final InventoryMovementService movementService;

    @PostMapping("/process")
    public ResponseEntity<ResponseStructure<InventoryMovementResponse>> processMovement(
            @RequestBody InventoryMovementRequest inventoryMovementRequest)
    {

        InventoryMovementResponse movementResponse = movementService.processInventoryMovement(inventoryMovementRequest); // Pass the DTO

        return ResponseBuilder.success(HttpStatus.CREATED, "Inventory Movement processed successfully!", movementResponse);
    }

    @PostMapping("/summary")
    public ResponseEntity<ListResponseStructure<InventoryMovementSummaryResponse>> getSummary(
            @RequestBody InventoryMovementSummaryRequest summaryRequest)
    {
        List<InventoryMovementSummaryResponse> summary = movementService.getInventoryMovementSummary(summaryRequest);
        return ResponseBuilder.success(HttpStatus.OK, "Inventory Movement Summary fetched successfully", summary);
    }

}