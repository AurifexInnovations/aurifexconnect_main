package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InventoryResponse {

    private long itemId;

    private String itemName;
    private double itemQuantity;
    private String itemDescription;
    private double itemCost;
    private String categories;
    private double lowStockThreshold;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

}
