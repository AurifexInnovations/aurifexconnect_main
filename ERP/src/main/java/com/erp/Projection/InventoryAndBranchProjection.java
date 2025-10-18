package com.erp.Projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAndBranchProjection {
    private long itemId;
    private String itemName;
    private double itemQuantity;
    private String itemDescription;
    private double itemCost;
    private String categories;
    private double lowStockThreshold;
    private LocalDateTime createdAt;
    private String branchName;

}